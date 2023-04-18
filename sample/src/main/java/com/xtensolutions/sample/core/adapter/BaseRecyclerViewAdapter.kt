package com.xtensolutions.sample.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Vaghela Mithun R. on 01/11/20.
 * vaghela.mithun@gmail.com
 */
@SuppressLint("NotifyDataSetChanged")
abstract class BaseRecyclerViewAdapter<T>(private val ctx: Context, private var list: MutableList<T>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var inflater: LayoutInflater = LayoutInflater.from(ctx)
    var viewHolderClickListener: BaseViewHolder.ViewHolderClickListener? = null

    var selectedIndex = -1

    init {
        super.setHasStableIds(true)
    }

    fun select(index: Int) {
//        val prev = selectedIndex
//        notifyItemChanged(prev)
        selectedIndex = index
//        notifyItemChanged(selectedIndex)
        notifyDataSetChanged()
    }

    fun unselect(index: Int) {
        selectedIndex = -1
        notifyItemChanged(index)
    }

    fun getSelectedItem() = list?.let {
        if (it.isNotEmpty() && selectedIndex > -1)
            return@let it[selectedIndex]
        else return@let null
    }

    override fun getItemCount(): Int = list!!.size

    open fun getCount(): Int = list!!.size

    open fun getItem(position: Int): T = list!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    open fun updateList(newData: MutableList<T>) {
        list = newData
        notifyDataSetChanged()
    }

    open fun add(item: T) {
        list!!.add(item)
        notifyDataSetChanged()
    }

    open fun addAt(position: Int, item: T) {
        list!!.add(position, item)
        notifyItemChanged(position)
    }

    open fun addListAt(position: Int, items: MutableList<T>) {
        list!!.addAll(position, items)
        notifyDataSetChanged()
    }

    open fun replace(position: Int, item: T) {
        list!!.set(position, item)
        notifyItemChanged(position)
    }

    open fun addTop(items: List<T>?) {
        list!!.addAll(0, items!!)
        notifyDataSetChanged()
    }

    open fun addAtLast(items: List<T>?) {
        list!!.addAll(items!!)
        notifyDataSetChanged()
    }


    open fun getList(): List<T>? = list

    open fun remove(item: T) {
        list!!.remove(item)
        notifyDataSetChanged()
    }

    open fun remove(position: Int) {
        list!!.removeAt(position)
        notifyItemRemoved(position)
    }

    open fun clear() {
        list!!.clear()
        notifyDataSetChanged()
    }
}