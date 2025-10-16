package com.yitong.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class YTAdapter<T> extends BaseAdapter {

	protected LayoutInflater inflater = null;
	protected Context mContext;
	private List<T> lst;

	public YTAdapter(Context context, List<T> lst) {
		this.mContext = context;
		this.lst = lst;
		this.inflater = LayoutInflater.from(context);
	}

	/**
	 * @Description 获取上下文对象
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * @Description 在数据集合头部添加一条数据
	 * @param data
	 *            传入一条T类型的数据
	 */
	public void addDataFirst(T data) {
		lst.add(0, data);
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description 在数据源的末尾位置增加一条数据
	 * @param data
	 *            传入一条T类型的数据
	 */
	public void addDataLast(T data) {
		lst.add(data);
		this.notifyDataSetChanged();
	}

	/**
	 * @Description 在数据集合头部添加一些数据
	 * @param datas
	 *            传入一个 T类型的数据集合
	 */
	public void addDatasToFirst(List<T> datas) {
		lst.addAll(0, datas);
		this.notifyDataSetChanged();
	}

	/**
	 * 在数据集合尾部添加一些数据
	 * 
	 * @param datas
	 *            传入一个 T 类型的数据集合
	 */
	public void addDatasToLast(List<T> datas) {
		lst.addAll(datas);
		this.notifyDataSetChanged();
	}

	/**
	 * 在数据集合某处替换为另外一个数据
	 * 
	 * @Description
	 * @param index
	 *            替换位置
	 * @param data
	 *            替换的数据
	 */
	public void replace(int index, T data) {
		if (index < 0)
			return;
		if (index > lst.size() - 1)
			return;
		lst.set(index, data);
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description 设置数据集合
	 * @param list
	 */
	public void setList(List<T> list) {
		this.lst = list;
		this.notifyDataSetChanged();
	}

	private YTAdapter() {
		super();
	}

	/**
	 * @Description 获取数据集合
	 * @return
	 */
	public List<T> getList() {
		return lst;
	}

	/**
	 * 
	 * @Description 获取某个位置T类型数据
	 * @param position
	 *            数据位置
	 * @return T类型数据
	 */
	public T getItemT(int position) {
		T obj = null;
		if (lst != null && position < lst.size())
			obj = lst.get(position);

		return obj;
	}

	/**
	 * @Description 删除一个T类型数据
	 * @param data
	 *            被删除的T类型数据对象
	 */
	public void delData(T data) {
		if(lst.remove(data))
			this.notifyDataSetChanged();
	}

	/**
	 * @Description 删除具体位置的T类型数据
	 * @param position
	 *            删除数据的位置
	 */
	public void delData(int position) {
		if (lst.size() <= 0)
			return;
		if (position < 0)
			return;
		if (position > lst.size() - 1)
			return;

		lst.remove(position);
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description 清除数据集合的所有数据
	 */
	public void clearData(boolean ...b) {
		if (lst != null)
			lst.clear();
		if(b.length > 0 && b[0])
			this.notifyDataSetChanged();
	}

	/**
	 * @Description 获取数据集合总数量
	 */
	@Override
	public int getCount() {
		return lst == null ? 0 : lst.size();
	}

	/**
	 * 
	 * @Description 获取该下标的数据对象，为 T 类型
	 */
	@Override
	public Object getItem(int position) {
		return lst == null ? null : lst.get(position);
	}

	/**
	 * @Description 获取一个下标
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @Description // 获取界面组件
	 * @return
	 */
	public abstract int[] getFindViewByIDs(int position);

	/**
	 * 
	 * @Description 获取布局资源
	 * @param position
	 * @return
	 */
	public abstract int getLayout(int position);

	/**
	 * 
	 * @Description 渲染数据
	 */
	public abstract void renderData(View convertView, int position, YTViewHolder vh);

	private final View getResourceView(int id) {
		return inflater.inflate(id, null);
	}

	@SuppressWarnings("unchecked")
	public final <V extends View> V findById(View view, int id){
		if(null == view)
			return null;
		YTViewHolder hold = (YTViewHolder) view.getTag();
		return (V)hold.getView(View.class, id);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		YTViewHolder vh;
		if (convertView == null) {
			vh = new YTViewHolder();
			convertView = getResourceView(this.getLayout(position));// 获取布局资源
			if (convertView == null)
				return null;
			int[] idAry = this.getFindViewByIDs(position);// 获取界面组件
			if (idAry == null)
				idAry = new int[] {};
			for (int id : idAry) {
				vh.setView(id, convertView.findViewById(id)); // 资源id作为key,缓存界面中的组件
			}
			convertView.setTag(vh);
		} else {
			vh = (YTViewHolder) convertView.getTag();
		}

		this.renderData(convertView,position, vh); // 继承类中的方法,完成数据到界面组件的赋值
		return convertView;
	}
}
