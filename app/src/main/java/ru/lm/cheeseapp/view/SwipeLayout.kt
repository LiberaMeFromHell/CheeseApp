package ru.lm.cheeseapp.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.lm.cheeseapp.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class SwipeHelper(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


    private val BUTTON_WIDTH = 200
    private var recyclerView: RecyclerView? = null
    private var buttons: MutableList<UnderlayButton>? = null
    private var gestureDetector: GestureDetector? = null
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private var buttonsBuffer: MutableMap<Int, List<UnderlayButton>?>? =
        null
    private var recoverQueue: Queue<Int>? = null

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons!!) {
                if (button.onClick(e.x, e.y)) break
            }
            return true
        }
    }

    private val onTouchListener = OnTouchListener { view, e ->
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(e.rawX.toInt(), e.rawY.toInt())
        val swipedViewHolder =
            recyclerView!!.findViewHolderForAdapterPosition(swipedPos)
        val swipedItem: View = swipedViewHolder!!.itemView
        val rect = Rect()
        swipedItem.getGlobalVisibleRect(rect)
        if (e.action == MotionEvent.ACTION_DOWN || e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y && rect.bottom > point.y) gestureDetector!!.onTouchEvent(e) else {
                recoverQueue?.add(swipedPos)
                swipedPos = -1
                recoverSwipedItem()
            }
        }
        false
    }


    init {
        buttons = ArrayList()
        gestureDetector = GestureDetector(context, gestureListener)
        buttonsBuffer = HashMap()
        recoverQueue = object : LinkedList<Int>() {
            override fun add(o: Int): Boolean {
                return if (contains(o)) false else super.add(o)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipedPos != pos) recoverQueue?.add(swipedPos)
        swipedPos = pos
        if (buttonsBuffer!!.containsKey(swipedPos)) buttons =
            (buttonsBuffer!![swipedPos] as MutableList) else buttons!!.clear()
        buttonsBuffer!!.clear()
        swipeThreshold = 0.5f * buttons!!.size * BUTTON_WIDTH
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView: View = viewHolder.itemView
        if (pos < 0) {
            swipedPos = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: List<UnderlayButton>? = ArrayList()
                if (!buttonsBuffer!!.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer!![pos] = buffer
                } else {
                    buffer = buttonsBuffer!![pos]
                }
                translationX = dX * buffer!!.size * BUTTON_WIDTH / itemView.getWidth()
                drawButtons(recyclerView.context,c, itemView, buffer, translationX)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!recoverQueue!!.isEmpty()) {
            val pos: Int = recoverQueue!!.poll()
            if (pos > -1) {
                recyclerView!!.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    private fun drawButtons(
        context: Context,
        c: Canvas,
        itemView: View,
        buffer: List<UnderlayButton>?,
        dX: Float
    ) {
        var right: Float = itemView.right.toFloat()
        val dButtonWidth = -1 * dX / buffer!!.size
        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                context,
                c,
                RectF(
                    left,
                    itemView.getTop().toFloat(),
                    right,
                    itemView.getBottom().toFloat()
                )
            )
            right = left
        }
    }

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        this.recyclerView!!.setOnTouchListener(onTouchListener)
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(this.recyclerView)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder?,
        underlayButtons: List<UnderlayButton>?
    )

    interface UnderlayButtonClickListener {
        fun onClick(pos: Int)
    }

    class UnderlayButton(
        private val imageResId: Int,
        private val color: Int,
        private val clickListener: UnderlayButtonClickListener
    ) {
        private var pos = 0
        private var clickRegion: RectF? = null
        fun onClick(x: Float, y: Float): Boolean {
            if (clickRegion != null && clickRegion!!.contains(x, y)) {
                clickListener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(context: Context, c: Canvas, rect: RectF) {
            val p = Paint()
            // Draw background
            p.color = color
            c.drawRect(rect, p)
            p.color = Color.WHITE
            p.textSize = Resources.getSystem().displayMetrics.density * 12

            //Draw IC
            val x1 = rect.left + (rect.right - rect.left)/3
            val y1 = rect.top + (rect.bottom - rect.top)/3
            val x2 = rect.left + (rect.right - rect.left)/3*2
            val y2 = rect.top + (rect.bottom - rect.top)/3*2

            val d = context.resources.getDrawable(imageResId, null)
            d.setBounds(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
            d.draw(c)
        }

    }
}
