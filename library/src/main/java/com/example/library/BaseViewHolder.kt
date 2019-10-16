package com.example.library

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.annotation.RequiresApi
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView


/**
 * ViewHolder基类
 * Created by 陈健宇 at 2019/5/30
 */
open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val mViews: SparseArrayCompat<View> = SparseArrayCompat()//缓存itemView中所有的子View
    var adapter: BaseAdapter<*>? = null


    fun setText(id: Int, text: String): BaseViewHolder {
        val textView = getView<TextView>(id)
        textView?.text = text
        return this
    }

    fun setImageResource(id: Int, imageId: Int): BaseViewHolder {
        val imageView = getView<ImageView>(id)
        imageView?.setImageResource(imageId)
        return this
    }

    fun setImageBitmap(id: Int, bitmap: Bitmap): BaseViewHolder {
        val imageView = getView<ImageView>(id)
        imageView?.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(id: Int, drawable: Drawable): BaseViewHolder {
        val imageView = getView<ImageView>(id)
        imageView?.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundResource(id: Int, backgroundResId: Int): BaseViewHolder {
        val view = getView<View>(id)
        view?.setBackgroundResource(backgroundResId)
        return this
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setBackgroundDrawable(id: Int, drawable: Drawable): BaseViewHolder {
        val view = getView<View>(id)
        view?.background = drawable
        return this
    }

    fun setBackgroundColor(id: Int, @ColorInt color: Int): BaseViewHolder {
        val view = getView<View>(id)
        view?.setBackgroundColor(color)
        return this
    }

    fun setChildClickListener(id: Int): BaseViewHolder {
        val view = getView<View>(id) ?: return this
        if (!view.isClickable) view.isClickable = true
        view.setOnClickListener { v ->
            adapter?.onItemChildClickListener?.onItemChildClickListener(adapter, v, this.layoutPosition)
        }
        return this
    }

    fun setChildClickListener(id: Int, listener: View.OnClickListener): BaseViewHolder {
        val view = getView<View>(id) ?: return this
        view.setOnClickListener(listener)
        return this
    }

    fun setChildLongListener(id: Int) : BaseViewHolder{
        val view = getView<View>(id) ?: return this
        if (!view.isClickable) view.isClickable = true
        view.setOnLongClickListener { v ->
            adapter?.onItemChildLongListener?.onItemChildLongListener(adapter, v, this.layoutPosition) ?: false
        }
        return this
    }

    fun setChildLongListener(id: Int, listener: View.OnLongClickListener): BaseViewHolder {
        val view = getView<View>(id) ?: return this
        view.setOnLongClickListener(listener)
        return this
    }


    fun setGone(@IdRes viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    fun setVisibility(id: Int, Visibility: Int): BaseViewHolder {
        val view = getView<View>(id)
        view?.visibility = Visibility
        return this
    }

    /**
     * 通过id从缓存中获取view实例，如果缓存没有，就从itemView中获取
     * @param id 要获取的view的id
     * @return 获取到的view
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T? {
        var view = mViews.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            mViews.put(id, view)
        }
        return view as T?
    }

}
