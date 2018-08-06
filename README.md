# PraiseTextView
**即刻点赞的原效果**
![](http://mmbiz.qpic.cn/mmbiz_gif/yj4Eg7LNy0KIHeKUrqKcBH2goKcAMGEiauSPlW68F8358g6CXBWriaqpdCxotutpJ4tDOJw7SM87yCOk3K6ktfyg/0?wx_fmt=gif)
**仿写效果**
![](http://of7f69tnj.bkt.clouddn.com/%E4%BB%BF%E5%8D%B3%E5%88%BB%E7%82%B9%E8%B5%9E.gif)
- - -
重新对类进行了抽离，只需要写需要关注的地方，同时把继承TextView改为继承View,增加扩展度。
- DrawableView也可以经常作用于小icon和TextView组合的场景(如果是TextView的话，是没法控制Drawable的大小和缩放的)
- 动画的话只需要关注继承BaseDrawableAnimator的部分和继承BaseTextAnimator的部分，一些注释也已经标注上去
- 为了方便起见，原来的老代码不做删除，迁移到praise_old包下，新代码在praise包下(新代码没有复刻即刻的效果，主要在于一个掌握方法，掌握了方法，模不模仿也就不重要了)
- 欢迎使用，多提issue
