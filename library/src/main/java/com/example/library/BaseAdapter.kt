package com.example.library

import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout


import com.example.library.anim.AlphaAnim
import com.example.library.anim.ScaleAnim
import com.example.library.anim.IAnim
import com.example.library.anim.SlideFormLeftAnim
import com.example.library.antation.AnimType
import com.example.library.loadmore.ILoadMoreViewProvide
import com.example.library.loadmore.LoadMoreHelper
import com.example.library.multiple.*
import kotlin.collections.ArrayList

/**
 * Adapter基类
 * Created by 陈健宇 at 2019/5/30
 */
open class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder> {

    companion object {
        private val  TAG = BaseAdapter::class.java.simpleName
        private const val LAYOUT_INIT_ID = -1
        const val TYPE_BASE = 0x100000
        const val TYPE_HEADER_VIEW = 0x110000
        const val TYPE_LOAD_MORE_VIEW = 0x111000
        const val TYPE_EMPTY_VIEW = 0x111100
        const val ANIM_ALPHA = 0x000000
        const val ANIM_SCALE = 0x000001
        const val ANIM_SLIDE_FROM_LEFT = 0x000002
    }

    private val adapterDelegateManager: MultiItemDelegateManager<T> = MultiItemDelegateManager()
    private var mHeaderView: LinearLayout? = null
    private var mEmptyView: FrameLayout? = null
    private var mLoadMoreHelper: LoadMoreHelper? = null
    private var mItemAnim: IAnim? = null
    private var isOpenItemAnim = false
    private var mLastItemAnimPosition = -1

    private val headerViewCount: Int
        get() = if (mHeaderView != null && mHeaderView?.childCount != 0) 1 else 0
    private val loadMoreViewCount: Int
        get() = if(onLoadMoreListener != null && isLoadMoreEnable && !datas.isEmpty()) 1 else 0
    private val loadMoreViewPosition: Int
        get() = headerViewCount + datas.size
    private val emptyViewCount: Int
        get() = if(mEmptyView != null && mEmptyView?.childCount != 0 && isUseEmptyView && datas.isEmpty()) 1 else 0

    var isAlwaysItemAnim = false //设置是否总是加载item动画
    var isUseEmptyView = false//是否使用空布局
    var isLoadMoreEndToLoadMoreEnable = false
    var isLoadMoreFailToLoadMoreEnable = false
    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemChildClickListener: OnItemChildClickListener? = null
    var onItemChildLongListener: OnItemChildLongListener? = null
    var onLoadMoreListener: OnLoadMoreListener? = null
        set(value) {
            isLoading = false
            isLoadMoreEnable = true
            field = value
        }
    var layoutId : Int = LAYOUT_INIT_ID
       set(@LayoutRes value){
           field = value
       }
    var datas : MutableList<T>
        set(value) {
            field = if(value.isNullOrEmpty()) ArrayList<T> () else value
            notifyDataSetChanged()
        }
    var isLoadMoreEnable = false
        set(value) {
            val oldLoadMoreCount = loadMoreViewCount
            field = value
            val curLoadMoreCount = loadMoreViewCount
            if(curLoadMoreCount > oldLoadMoreCount){//增加
                mLoadMoreHelper?.curStatus = LoadMoreHelper.STATUS_LOADING_COMPLETE
                notifyItemInserted(loadMoreViewPosition)
            }else if(curLoadMoreCount < oldLoadMoreCount){//减少
                isLoading = false
                notifyItemRemoved(loadMoreViewPosition)
            }
            //没有改变的情况不要 notify，避免造成数据源不同步导致崩溃
        }
    var isLoading = false
        private set

    init {
        //type值占位，防止外部添加重复的type值
        addItemDelegate(TYPE_BASE, BaseDelegate())
                .addItemDelegate(TYPE_HEADER_VIEW, HeaderDelegate())
                .addItemDelegate(TYPE_LOAD_MORE_VIEW, LoadMoreDelegate())
    }

    constructor(datas : MutableList<T>) : this(datas, 0)
    constructor(layoutId : Int) : this(ArrayList<T>(), layoutId)
    constructor(datas : MutableList<T>, layoutId : Int) {
        this.datas = datas
        this.layoutId = layoutId
    }

    protected open fun onBindView(holder: BaseViewHolder, item: T) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        Log.d(TAG, "onCreateViewHolder, type = $viewType")
        val holder: BaseViewHolder = when(viewType){
            TYPE_HEADER_VIEW ->
                BaseViewHolder(mHeaderView!!)
            TYPE_EMPTY_VIEW ->
                BaseViewHolder(mEmptyView!!)
            TYPE_BASE ->
                BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
            TYPE_LOAD_MORE_VIEW ->
                BaseViewHolder(getLoadMoreView(parent)!!)
            else ->
                adapterDelegateManager.onCreateViewHolder(parent, viewType)
        }
        bindItemClickListener(holder)
        return holder
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder, pos = $position")
        val viewType = holder.itemViewType
        val dataPosition = getDataPosition(position)
        when(viewType){
            TYPE_HEADER_VIEW, TYPE_EMPTY_VIEW ->{
                return
            }
            TYPE_LOAD_MORE_VIEW ->{
                onLoadMoreCallback(dataPosition)
                mLoadMoreHelper?.show(holder)
            }
            TYPE_BASE ->{
                applyItemAnimation(holder.itemView, dataPosition)
                onBindView(holder, datas[dataPosition])
            }
            else ->{
                applyItemAnimation(holder.itemView, dataPosition)
                adapterDelegateManager.onBindViewHolder(holder, datas[dataPosition], dataPosition)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val dataPosition = getDataPosition(position)
        val datasCount = datas.size
        Log.d(TAG, "getItemViewType, pos = $position, itemCount = $itemCount")
        return if (position < headerViewCount) {
            TYPE_HEADER_VIEW
        } else if(emptyViewCount != 0){
            TYPE_EMPTY_VIEW
        } else if(dataPosition < datasCount){
            val item = datas[dataPosition]
            val viewType = adapterDelegateManager.getItemViewType(item, dataPosition)
            if (viewType == TYPE_BASE && layoutId == LAYOUT_INIT_ID)
                throw NullPointerException("No IMultiItemDelegate added that matches position = $position in data source")
            else
                viewType
        }else{
            TYPE_LOAD_MORE_VIEW
        }
    }

    override fun getItemCount(): Int {
        return if (datas.isEmpty())
            emptyViewCount + headerViewCount
        else
            datas.size + headerViewCount + loadMoreViewCount
    }

    private fun bindItemClickListener(holder: BaseViewHolder?) {
        if (holder == null) return
        holder.adapter = this
        val itemView = holder.itemView
        itemView.setOnClickListener { v ->
            onItemClickListener?.onItemClick(this, v, getDataPosition(holder.layoutPosition))
        }
        itemView.setOnLongClickListener { v ->
            onItemLongClickListener?.onItemLongClick(this, v, getDataPosition(holder.layoutPosition)) ?: false
        }
    }


    private fun getLoadMoreView(parent: ViewGroup): View?{
        initLoadMoreHelper()
        mLoadMoreHelper?.updateLoadMoreView(parent)
        val loadMoreView = mLoadMoreHelper?.loadMoreView
        isLoading = false
        return loadMoreView
    }

    private fun onLoadMoreCallback(dataPos: Int){
        if(loadMoreViewCount < 0) return
        if(dataPos < datas.size) return
        if(mLoadMoreHelper?.curStatus == LoadMoreHelper.STATUS_LOADING_COMPLETE && !isLoading){
            isLoading = true
            mLoadMoreHelper?.curStatus = LoadMoreHelper.STATUS_LOADING
            onLoadMoreListener?.onLoadMore()
        }
    }

    private fun getDataPosition(position: Int): Int {
        val dataPosition = position - headerViewCount
        return if (dataPosition < 0) position else dataPosition
    }

    private fun applyItemAnimation(itemView: View, position: Int) {
        if (!isOpenItemAnim) return
        if (!isAlwaysItemAnim) {
            if (mLastItemAnimPosition >= position) return
            mLastItemAnimPosition = position
        } else {
            mLastItemAnimPosition = -1
        }
        mItemAnim?.applyAnimation(itemView)
    }

    private fun openItemAnim(@AnimType animType: Int) {
        if (isOpenItemAnim) return
        isOpenItemAnim = true
        changeItemAnim(animType)
    }

    private fun initLoadMoreHelper() {
        if (mLoadMoreHelper == null) {
            mLoadMoreHelper = LoadMoreHelper()
        }
    }

    private fun removeParent(view: View?) {
        val viewParent = view?.parent
        if (viewParent is ViewGroup) {
            viewParent.removeView(view)
        }
    }

    /**
     * 如果item没有填充满recyclerView，移除掉加载更多视图，避免触发加载更多逻辑
     * ps：这个方法要在设置数据源之后调用，否则无效
     */
    fun disableLoadMoreIfNotFill(recyclerView: RecyclerView){
        isLoadMoreEnable = false
        val layoutManager = recyclerView.layoutManager
        var result = true
        recyclerView.postDelayed({
            if(layoutManager is LinearLayoutManager){
                result = (layoutManager.findLastCompletelyVisibleItemPosition() + 1) != itemCount
            }else if(layoutManager is StaggeredGridLayoutManager){
                val postions = IntArray(layoutManager.spanCount)
                layoutManager.findLastCompletelyVisibleItemPositions(postions)
                var maxPos = Int.MIN_VALUE
                for(pos in postions){
                    if(pos > maxPos){
                        maxPos = pos
                    }
                }
                result = (maxPos + 1) != itemCount
            }
            isLoadMoreEnable = result
        } , 50)
    }

    /**
     * 设置自定义的LoadMoreView
     * ps：这个方法要在设置适配器之后调用，否则无效
     */
    fun setLoadMoreProvide(loadMoreViewProvide: ILoadMoreViewProvide){
        initLoadMoreHelper()
        mLoadMoreHelper!!.updateLoadMoreProvide(loadMoreViewProvide)
    }

    /**
     * 加载更多完成，下次滑到底部时还会触发加载更多逻辑
     */
    fun loadingComplete(){
        if(loadMoreViewCount == 0) return
        isLoading = false
        mLoadMoreHelper?.curStatus = LoadMoreHelper.STATUS_LOADING_COMPLETE
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 加载更多结束，下次滑到底部时不会触发加载更多逻辑，但会显示loadingEnd视图
     */
    fun loadingEnd(){
        if(loadMoreViewCount == 0) return
        isLoading = false
        mLoadMoreHelper?.curStatus = LoadMoreHelper.STATUS_LOADING_END
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 加载更多失败，下次滑到底部时不会触发加载更多逻辑，但会显示loadingFail视图
     */
    fun loadingFail(){
        if(loadMoreViewCount == 0) return
        isLoading = false
        mLoadMoreHelper?.curStatus = LoadMoreHelper.STATUS_LOADING_FAIL
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * 不是同一个引用, 清空列表，重新设置数据源
     */
    fun replaceDatas(datas: MutableList<T>?) {
        if(datas.isNullOrEmpty()) return
        if (datas !== this.datas) {
            this.datas.clear()
            this.datas.addAll(datas)
        }
        notifyDataSetChanged()
    }

    /**
     * 在数据源末尾添加很多数据
     */
    fun addDatas(datas: MutableList<T>){
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    /**
     * 在数据源末尾添加一个数据
     */
    fun addData(item: T){
        addData(item, datas.size)
    }

    /**
     * 根据位置添加一个数据
     */
    fun addData( item: T, @IntRange(from = 0) pos : Int){
        datas.add(pos, item)
        notifyItemInserted(headerViewCount + pos)
    }

    /**
     * 根据位置更新一个数据
     */
    fun setData(item: T, @IntRange(from = 0) pos: Int){
        datas[pos] = item
        notifyItemChanged(headerViewCount + pos)
    }


    /**
     * 根据位置移除一个数据
     */
    fun removeData(@IntRange(from = 0) pos: Int){
        datas.removeAt(pos)
        notifyItemRemoved(headerViewCount + pos)
    }

    /**
     * 移除一个数据
     */
    fun removeData(item: T){
        datas.remove(item)
        notifyDataSetChanged()
    }


    /**
     * 打开加载item的动画，都是默认值
     */
    fun openItemAnim() {
        openItemAnim(ANIM_SLIDE_FROM_LEFT)
        isAlwaysItemAnim = false
    }

    /**
     * 打开加载item的动画，动画默认
     * @param isAlways 是否每次都加载item动画
     */
    fun openItemAnim(isAlways: Boolean) {
        openItemAnim(ANIM_SLIDE_FROM_LEFT)
        isAlwaysItemAnim = isAlways
    }

    /**
     * 关闭加载item的动画
     */
    fun closeItemAnim() {
        if (!isOpenItemAnim) return
        isOpenItemAnim = false
        isAlwaysItemAnim = false
        mItemAnim = null
    }

    /**
     * 根据内置anim的类型改变加载item的动画
     * @param animType 内置anim的类型
     */
    fun changeItemAnim(@AnimType animType: Int) {
        when (animType) {
            ANIM_ALPHA -> mItemAnim = AlphaAnim()
            ANIM_SCALE -> mItemAnim = ScaleAnim()
            ANIM_SLIDE_FROM_LEFT -> mItemAnim = SlideFormLeftAnim()
            else -> mItemAnim = SlideFormLeftAnim()
        }
    }

    /**
     * 根据给定的动画改变加载item的动画
     * @param anim 继承自IAnim的动画
     */
    fun changeItemAnim(anim: IAnim) {
        this.mItemAnim = anim
    }

    /**
     * 添加一个空布局视图
     * @param emptyView emptyView
     */
    fun addEmptyView(emptyView: View){
        if(mEmptyView == null){
            mEmptyView = FrameLayout(emptyView.context)
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            val emptyViewParams = emptyView.layoutParams

            if (emptyViewParams != null) {
                layoutParams.width = emptyViewParams.width
                layoutParams.height = emptyViewParams.height
            }
            mEmptyView?.layoutParams = layoutParams
        }
        mEmptyView?.removeAllViews()
        mEmptyView?.addView(emptyView)
        removeParent(mEmptyView)
        isUseEmptyView = true
        if(emptyViewCount != 0) notifyDataSetChanged()
    }


    /**
     * 添加一个HeaderView，可重复添加
     * @param headerView HeaderView
     */
    fun addHeaderView(headerView: View) {
        this.addHeaderView(headerView, -1)
    }

    /**
     * 根据index添加一个HeaderView
     * @param headerView HeaderView
     * @param index HeaderView在布局中的位置
     */
    fun addHeaderView(headerView: View, index: Int) {
        var pos = index
        if (mHeaderView == null) {
            mHeaderView = LinearLayout(headerView.context)
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mHeaderView?.layoutParams = params
            mHeaderView?.orientation = LinearLayout.VERTICAL
        }
        val childCount = mHeaderView!!.childCount
        if (pos < 0 || pos > childCount) {
            pos = childCount
        }
        mHeaderView?.addView(headerView, pos)
        removeParent(mHeaderView)
        if (mHeaderView?.childCount == 1) notifyItemInserted(0)
    }

    /**
     * 移除一个HeaderView
     * @param headerView 你要移除的headerView
     */
    fun removeHeaderView(headerView: View) {
        if (headerViewCount < 1) return
        val index = mHeaderView!!.indexOfChild(headerView)
        removeHeaderView(index)
    }

    /**
     * 移除一个HeaderView
     * @param index 你要移除的headerView的索引
     */
    fun removeHeaderView(index: Int) {
        if (headerViewCount < 1 || index < 0 || index > mHeaderView!!.childCount) return
        mHeaderView?.removeViewAt(index)
        if (headerViewCount < 1) notifyItemRemoved(0)
    }


    /**
     * 移除整个HeaderView
     */
    fun removeHeaderView() {
        if (headerViewCount < 1) return
        mHeaderView?.removeAllViews()
        notifyItemRemoved(0)
    }

    /**
     * 添加item的AdapterDelegte，默认viewType
     * @param delegate item的AdapterDelegte
     */
    fun addItemDelegate(delegate: IMultiItemDelegate<T>): BaseAdapter<T> {
        adapterDelegateManager.addDelegate(delegate)
        return this
    }

    /**
     * 根据给定的带viewType添加item的AdapterDelegte
     * @param viewType item的类型
     * @param delegate item的AdapterDelegte
     */
    fun addItemDelegate(viewType: Int, delegate: IMultiItemDelegate<T>): BaseAdapter<T> {
        adapterDelegateManager.addDelegate(viewType, delegate)
        return this
    }

    /**
     * 加载更多回调监听接口
     */
    interface OnLoadMoreListener{
        /**
         * 加载更多回调方法
         */
        fun onLoadMore()
    }

    /**
     * item单击事件监听接口
     */
    interface OnItemClickListener {
        /**
         * item单击事件监听接口回调方法
         * @param adapter 适配器
         * @param view position对应的itemView
         * @param position itemView在源数据中的索引
         */
        fun onItemClick(adapter: BaseAdapter<*>?, view: View, position: Int)
    }

    /**
     * item长按事件监听接口
     */
    interface OnItemLongClickListener {
        /**
         * item长按事件监听接口回调方法
         * @param adapter 适配器
         * @param view position对应的itemView
         * @param position itemView在源数据中的索引
         * @return true表示itemView消费这个长按事件
         */
        fun onItemLongClick(adapter: BaseAdapter<*>?, view: View, position: Int): Boolean
    }

    /**
     * item的子控件的单击接口
     */
    interface OnItemChildClickListener {
        /**
         * item的子控件的单击接口回调方法
         * @param adapter 适配器
         * @param view position对应的item的子控件
         * @param position itemView在源数据中的索引
         */
        fun onItemChildClickListener(adapter: BaseAdapter<*>?, view: View, position: Int)
    }

    /**
     * item的子控件的长按接口
     */
    interface OnItemChildLongListener {
        /**
         * item的子控件的长按接口回调方法
         * @param adapter 适配器
         * @param view position对应的item的子控件
         * @param position itemView在源数据中的索引
         * @return true表示item的子控件消费这个长按事件
         */
        fun onItemChildLongListener(adapter: BaseAdapter<*>?, view: View, position: Int): Boolean
    }
}
