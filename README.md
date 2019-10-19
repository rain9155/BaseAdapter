
封装RecyclerView的Adapter，减少Adapter重复代码的编写，支持多种类型的itemType、自动加载更多、添加空视图。

## Pre

RecyclerView已经成为了Android开发中列表控件的首选，它可以取代ListView，GridView和ViewPager，听说最近新出的ViewPager2底层都用RecyclerView实现了，所以我们不可避免的要为每一个RecyclerView编写Adapter的代码，但是日子久了你会发现你一直都在做重复代码的编写，特别是每次根据itemType，添加不同的item时，你都要写一遍上次一样的代码，所以为自己封装一个Adapter是很有必要的。

## How to do？

对于多itemType的添加，我使用了[AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates)中使用的方式，这位作者在这篇文章[create adapter hell escape](http://hannesdorfmann.com/android/adapter-delegates)中讲解了这种方式的原理，使用一个Manager管理所有itemType的ViewHolder的create和bind，通过对接口的实现，让外部来创建itemType













## How to install？

## How to use？
