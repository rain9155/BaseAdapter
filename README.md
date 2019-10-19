# BaseAdapter

[![Download](https://api.bintray.com/packages/rain9155/jianyu/baseadapter/images/download.svg)](https://bintray.com/rain9155/jianyu/baseadapter/_latestVersion)

### 封装RecyclerView的Adapter，减少Adapter重复代码的编写，支持多种类型的itemType、自动加载更多、添加emptyView和添加headerView。

## Pre

RecyclerView已经成为了Android开发中列表控件的首选，它可以取代ListView，GridView和ViewPager，听说最近新出的ViewPager2底层都用RecyclerView实现了，所以我们不可避免的要为每一个RecyclerView编写Adapter的代码，但是日子久了你会发现你一直都在做重复代码的编写，特别是每次根据itemType，添加不同的item时，你都要写一遍上次一样的代码，所以为自己封装一个Adapter是很有必要的，我就根据自己平常经常使用的几个itemType封装到BaseAdapter中，只需要实现一个方法就能完成以前编写十几行代码的功能。

## How to do？

你会发现自动加载更多、添加emptyView和添加headerView都是属于多itemType的一种，所以归根到底就是多itemType的封装，对于多itemType的封装，我使用了[AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates)中使用的方式，这位作者在这篇文章[create adapter hell escape](http://hannesdorfmann.com/android/adapter-delegates)中讲解了这种方式的原理，使用一个[Manager](https://github.com/rain9155/BaseAdapter/blob/master/library/src/main/java/com/example/library/multiple/MultiItemDelegateManager.kt)管理所有itemType的Delegate，用户通过对[Delegate](https://github.com/rain9155/BaseAdapter/blob/master/library/src/main/java/com/example/library/multiple/IMultiItemDelegate.kt)接口方法的不同实现，让外部来创建和绑定不同的itemType的ViewHolder，并且通过isForViewType这个方法的返回值true或false来决定这个item是否使用数据源中的这个position来在RecyclerView中的相应位置来展示自己，然后原本的Adapter中把 create和bind ViewHolder的任务都**委托**给了Manager来实现，因为Manager中持有所有用户添加的Delegate，Delegate会根据itemType来创建和绑定相应的ViewHolder。

## PreView

![adapter](/screenshots/adapter.gif)

其中头部绿色显示的是添加的HeaderView，接下来是数据源，一种颜色代表着一个itemType，拉到底部就会触发自动加载更多，最后清空数据后显示一个emptyView。

## How to install？

在app目录下的build.grade下添加：

```
implementation 'com.jianyu:baseadapter:1.0.2'
```

## How to use？

### 1、没有itemType

如果你不需要添加itemType，只是正常的使用Adapter，你只需要继承BaseAdapter，然后重写**onBindView**方法，在这个方法里面完成数据向ViewHolder的绑定，如下：

```java
public class DataAdapter extends BaseAdapter<String> {
    
    public DataAdapter(List<String> datas, int layoutId) {
        super(datas, layoutId);
    }

    @Override
    protected void onBindView(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_text, item);
    }
}
```

其中BaseViewHolder是根据封装了一些**setXX**方法的ViewHolder，不用我们手动findByView，例如这里的setText方法。

### 2、多itemType

添加多itemType，你只需要为每个item实现一个[Delegate](https://github.com/rain9155/BaseAdapter/blob/master/library/src/main/java/com/example/library/multiple/IMultiItemDelegate.kt)，如下：

```java
//itemType1
//position等于1或10时显示这个item
public class MultipleItem1 implements IMultiItemDelegate<String> {

    @Override
    public boolean isForViewType(String item, int position) {
        return position == 1 || position == 10;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi1, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindView(BaseViewHolder holder, String item, int position) {
        holder.setText(R.id.tv_text, items);
    }

}

//itemType2
//position等于5或30时显示这个item
public class MutipleItem2 implements IMultiItemDelegate<String> {

    @Override
    public boolean isForViewType(String item, int position) {
        return position == 5 || position == 30;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi2, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindView(BaseViewHolder holder, String item, int position) {
        holder.setText(R.id.tv_text, items);
    }
}

```

在isForViewType方法中根据position或item返回true或者false来决定你是否使用数据源中的这个position来在RecyclerView中的相应位置来展示item，position就是数据源中item的位置。

然后在你自己的Adapter构造函数或外部调用addItemDelegate方法把这个两个itemType的Delegate添加进Manager中，如下：

```java
public class DataAdapter extends BaseAdapter<String> {

    public DataAdapter(List<String> datas, int layoutId) {
        super(datas, layoutId);
        addItemDelegate(new MutipleItem2())
            .addItemDelegate(new MultipleItem1());

    }

    @Override
    protected void onBindView(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_text, item);
    }
}

或

mDataAdapter.addItemDelegate(new MutipleItem2())
    .addItemDelegate(new MultipleItem1());

```

你添加时还可以指定一个type值，如下：

```java
mDataAdapter.addItemDelegate(1, new MutipleItem2())
    .addItemDelegate(2, new MultipleItem1());
```

否则就会使用默认的type值。

### 3、添加和移除HeaderView

添加HeaderView，可以连续添加多个，如下：

```java
mDataAdapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header_view, null));
或
//指定位置添加
mDataAdapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header_view, null)， 0);

```

移除HeaderView，如下：

```java
//移除所有HeaderView
mDataAdapter.removeHeaderView();
或
//移除指定位置的HeaderView
mDataAdapter.removeHeaderView();

```

### 4、添加和关闭自动加载

默认不适用自动加载，开启自动加载只需要添加一个OnLoadMoreListener，如下：

```java
  mDataAdapter.setOnLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore");
                mHandler.postDelayed(() -> {
                    List<String> newDatas = new ArrayList<>();
                    getDatas(newDatas, 10);
                    mDataAdapter.addDatas(newDatas);
                    mDataAdapter.loadingComplete();
                    Log.d(TAG, "loadingComplete");
                }, 2000);
            }
    });
```

当处理完加载数据的逻辑后，需要调用loadingComplete或loadingEnd或loadingFail方法来结束自动加载，loadingComplete表示下次还有数据，loadingEnd表示下次没有数据了，loadingFail表示本次加载失败。

关闭自动加载，如下：

```java
mDataAdapter.setLoadMoreEnable(false);
```

### 5、添加和关闭EmptyView

默认不使用EmptyView，添加EmptyView，如下：

```java
mDataAdapter.addEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, null));
```

关闭EmptyView，如下：

```java
mDataAdapter.setUseEmptyView(false);
```

### 6、开启和关闭item进入动画

开启进入动画，如下：

```java
//使用默认的动画
mDataAdapter.openItemAnim();
```

如果要改变默认的进入动画，内置了3种选择，如下：

```java
//开启并更换动画，ANIM_ALPHA(渐显)、 ANIM_SCAL（缩放）、 ANIM_SLIDE_FROM_LEFT（从左边滑出来）
mDataAdapter.changeItemAnim(BaseAdapter.ANIM_ALPHA);
```

关闭加载动画，如下：

```java
mDataAdapter.closeItemAnim();
```

### 7、自定义

使用[策略模式](https://blog.csdn.net/Rain_9155/article/details/82949305)，替换掉内部的默认实现。

#### 1、自定义加载更多视图

自定义加载更多视图，实现[ILoadMoreViewProvide](https://github.com/rain9155/BaseAdapter/blob/master/library/src/main/java/com/example/library/loadmore/ILoadMoreViewProvide.kt)，如下：

```java
public class MyLoadMoreProvide implements ILoadMoreViewProvide {

    @Override
    public int getLoadMoreLayoutId() {
        return R.layout.load_more;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.loading;
    }

    @Override
    public int getLoadingEndViewId() {
        return R.id.loading_end;
    }

    @Override
    public int getLoadingFailViewId() {
        return R.id.loading_fail;
    }
}
```

然后添加进Adapter，如下：

```java
mDataAdapter.setLoadMoreProvide(new MyLoadMoreProvide());
```

#### 2、自定义item进入动画

自定义item进入动画，实现[IAnim](https://github.com/rain9155/BaseAdapter/blob/master/library/src/main/java/com/example/library/anim/IAnim.kt), 如下：

```java
class AlphaAnim extends IAnim {
    
	@Override
    public void applyAnimation(View itemView) {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(itemView, "alpha", 0.5f, 1f);
        alphaAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        alphaAnimation.start();
    }

}
```

然后添加进Adapter，如下：

```java
mDataAdapter.changeItemAnim(new AlphaAnim());
```

