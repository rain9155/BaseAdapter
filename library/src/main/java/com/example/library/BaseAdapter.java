package com.example.library;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.library.anim.AlphaAnim;
import com.example.library.anim.ScaleAnim;
import com.example.library.anim.Type;
import com.example.library.anim.IAnim;
import com.example.library.anim.SlideFormLeftAnim;
import com.example.library.config.AnimType;
import com.example.library.multiple.BaseMultiItemDelegate;
import com.example.library.multiple.IMultiItemDelegate;
import com.example.library.multiple.MultiItemDelegateManager;

import java.util.List;

/**
 * Adapter基类
 * Created by 陈健宇 at 2019/5/30
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder>{

    private static final String TAG = BaseAdapter.class.getSimpleName();
    public static final int TYPE_BASE = 0x000000;
    private static final int TYPE_HEADER_VIEW = 0x100000;

    private MultiItemDelegateManager<T> adapterDelegateManager;
    private LinearLayout mHeaderView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongListener mOnItemChildLongListener;
    private IAnim mItemAnim;
    private boolean isOpenItemAnim;
    private boolean isAlwaysItemAnim;
    private int mLastItemAnimPosition = -1;

    protected List<T> mDatas;
    protected int mLayoutId;

    public BaseAdapter(List<T> datas) {
        this(datas, -1);
    }

    public BaseAdapter(int layoutId) {
        this(null, layoutId);
    }

    public BaseAdapter(List<T> datas, int layoutId) {
        mDatas = datas;
        mLayoutId = layoutId;
        adapterDelegateManager = new MultiItemDelegateManager<>();
        addItemAdapterDelegte(TYPE_BASE, new BaseMultiItemDelegate<>());
    }

    protected void onBindView(BaseViewHolder holder, T item){}

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder;
        if(viewType == TYPE_HEADER_VIEW){
            holder = new BaseViewHolder(mHeaderView);
        }else if(viewType == TYPE_BASE){
            holder = new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false));
        }else {
            holder = adapterDelegateManager.onCreateViewHolder(parent, viewType);
        }
        bindItemClickLisitener(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if(viewType == TYPE_HEADER_VIEW) return;
        int dataPosition = getDataPostion(position);
        View itemView = holder.getItemView();
        applyItemAnimation(itemView, dataPosition);
        if(viewType == TYPE_BASE){
            onBindView(holder, mDatas.get(dataPosition));
        }else{
            adapterDelegateManager.onBindViewHolder(holder, mDatas.get(dataPosition), dataPosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < getHeaderViewCount()){
            return TYPE_HEADER_VIEW;
        }else {
            int dataPosition = getDataPostion(position);
            int viewType = adapterDelegateManager.getItemViewType(mDatas.get(dataPosition), dataPosition);
            if(viewType == TYPE_BASE && mLayoutId == -1)
                throw new NullPointerException("No IMultiItemDelegate added that matches position = " + position + " in data source");
            return viewType;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? getHeaderViewCount() : mDatas.size() + getHeaderViewCount();
    }


    private void bindItemClickLisitener(final BaseViewHolder holder) {
        if(holder == null) return;
        final View itemView = holder.getItemView();
        if(itemView == null) return;
        holder.setAdapter(this);
        if(mOnItemClickListener != null){
            itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(this, itemView, getDataPostion(holder.getLayoutPosition())));
        }
        if(mOnItemLongClickListener != null){
            itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(this, itemView, getDataPostion(holder.getLayoutPosition())));
        }
    }

    private int getHeaderViewCount(){
        if(mHeaderView == null || mHeaderView.getChildCount() == 0) return 0;
        return 1;
    }

    private int getDataPostion(int position) {
        int dataPosition = position - getHeaderViewCount();
        return dataPosition < 0 ? position : dataPosition;
    }

    private void applyItemAnimation(View itemView, int position) {
        if(!isOpenItemAnim) return;
        if(!isAlwaysItemAnim){
            if(mLastItemAnimPosition >= position) return;
            mLastItemAnimPosition = position;
        }else {
            mLastItemAnimPosition = -1;
        }
        if(mItemAnim != null){
            mItemAnim.applyAnimation(itemView);
        }
    }

    private void openItemAnimation(@Type int animType){
        if(isOpenItemAnim) return;
        isOpenItemAnim = true;
        changeItemAnimation(animType);
    }

    /**
     * 设置数据源
     */
    public void setDatas(List<T> datas) {
        mDatas = datas;
    }

    /**
     * 设置是否总是加载item动画
     */
    public void setAlwaysItemAnim(boolean alwaysItemAnim) {
        isAlwaysItemAnim = alwaysItemAnim;
    }

    /**
     * 打开加载item的动画，都是默认值
     */
    public void openItemAnimation(){
        openItemAnimation(AnimType.SLIDE_FROM_LEFT);
        setAlwaysItemAnim(false);
    }

    /**
     * 打开加载item的动画，动画默认
     * @param isAlways 是否每次都加载item动画
     */
    public void openItemAnimation(boolean isAlways){
        openItemAnimation(AnimType.SLIDE_FROM_LEFT);
        setAlwaysItemAnim(isAlways);
    }

    /**
     * 关闭加载item的动画
     */
    public void closeItemAnimation(){
        if(!isOpenItemAnim) return;
        isOpenItemAnim = false;
        isAlwaysItemAnim = false;
        mItemAnim = null;
    }

    /**
     * 根据内置anim的类型改变加载item的动画
     * @param animType 内置anim的类型
     */
    public void changeItemAnimation(@Type int animType){
        switch (animType){
            case AnimType.ALPHA:
                mItemAnim = new AlphaAnim();
                break;
            case AnimType.SCALE:
                mItemAnim = new ScaleAnim();
                break;
            case AnimType.SLIDE_FROM_LEFT:
                mItemAnim = new SlideFormLeftAnim();
                break;
            default:
                mItemAnim = new SlideFormLeftAnim();
                break;
        }
    }

    /**
     * 根据给定的动画改变加载item的动画
     * @param anim 继承自IAnim的动画
     */
    public void changeItemAnimation(IAnim anim){
        this.mItemAnim = anim;
    }

    /**
     * 添加一个HeaderView，可重复添加
     * @param headerView HeaderView
     */
    public void addHeaderView(View headerView){
      this.addHeaderView(headerView, -1);
    }

    /**
     * 根据index添加一个HeaderView
     * @param headerView HeaderView
     * @param index HeaderView在布局中的位置
     */
    public void addHeaderView(View headerView, int index){
        if(mHeaderView == null){
            mHeaderView = new LinearLayout(headerView.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHeaderView.setLayoutParams(params);
            mHeaderView.setOrientation(LinearLayout.VERTICAL);
        }
        int childCount = mHeaderView.getChildCount();
        if(index < 0 || index > childCount){
            index = childCount;
        }
        mHeaderView.addView(headerView, index);
        if(mHeaderView.getChildCount() == 1) notifyItemInserted(0);
    }

    /**
     * 移除一个HeaderView
     * @param headerView 你要移除的headerView
     */
    public void removeHeaderView(View headerView){
        if(getHeaderViewCount() < 1) return;
        int index = mHeaderView.indexOfChild(headerView);
        removeHeaderView(index);
    }

    /**
     * 移除一个HeaderView
     * @param index 你要移除的headerView的索引
     */
    public void removeHeaderView(int index){
        if(getHeaderViewCount() < 1 || index < 0 || index > mHeaderView.getChildCount()) return;
        mHeaderView.removeViewAt(index);
        if(getHeaderViewCount() < 1) notifyItemRemoved(0);
    }


    /**
     * 移除整个HeaderView
     */
    public void removeHeaderView(){
        if(getHeaderViewCount() < 1) return;
        mHeaderView.removeAllViews();
        notifyItemRemoved(0);
    }

    /**
     * 添加item的AdapterDelegte，默认viewType
     * @param delegate item的AdapterDelegte
     */
    public BaseAdapter<T> addItemAdapterDelegte(IMultiItemDelegate<T> delegate){
        adapterDelegateManager.addDelegte(delegate);
        return this;
    }

    /**
     * 根据给定的带viewType添加item的AdapterDelegte
     * @param viewType item的类型
     * @param delegate item的AdapterDelegte
     */
    public BaseAdapter<T> addItemAdapterDelegte(int viewType, IMultiItemDelegate<T> delegate){
        adapterDelegateManager.addDelegte(viewType, delegate);
        return this;
    }

    /**
     * 设置item的单击监听
     * @param onItemClickListener 单击监听接口实例
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置item的长按监听
     * @param onItemLongClickListener 长按监听接口实例
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 获得item子控件的单击监听接口实例
     */
    public OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * 设置item的子控件单击监听
     * @param onItemChildClickListener 单击监听接口实例
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * 获得item子控件的长按监听接口实例
     */
    public OnItemChildLongListener getOnItemChildLongListener() {
        return mOnItemChildLongListener;
    }

    /**
     * 设置item的子控件长按监听
     * @param onItemChildLongListener 长按监听接口实例
     */
    public void setOnItemChildLongListener(OnItemChildLongListener onItemChildLongListener) {
        mOnItemChildLongListener = onItemChildLongListener;
    }

    /**
     * item单击事件监听接口
     */
    public interface OnItemClickListener{
        /**
         * item单击事件监听接口回调方法
         * @param adapter 适配器
         * @param view position对应的itemView
         * @param position itemView在源数据中的索引
         */
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * item长按事件监听接口
     */
    public interface OnItemLongClickListener{
        /**
         * item长按事件监听接口回调方法
         * @param adapter 适配器
         * @param view position对应的itemView
         * @param position itemView在源数据中的索引
         * @return true表示itemView消费这个长按事件
         */
        boolean onItemLongClick(BaseAdapter adapter, View view, int position);
    }

    /**
     * item的子控件的单击接口
     */
    public interface OnItemChildClickListener{
        /**
         *  item的子控件的单击接口回调方法
         * @param adapter 适配器
         * @param view position对应的item的子控件
         * @param position itemView在源数据中的索引
         */
        void onItemChildClickListener(BaseAdapter adapter, View view, int position);
    }

    /**
     * item的子控件的长按接口
     */
    public interface OnItemChildLongListener{
        /**
         *  item的子控件的长按接口回调方法
         * @param adapter 适配器
         * @param view position对应的item的子控件
         * @param position itemView在源数据中的索引
         * @return true表示item的子控件消费这个长按事件
         */
        boolean onItemChildLongListener(BaseAdapter adapter, View view, int position);
    }

}
