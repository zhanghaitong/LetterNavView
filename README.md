## LetternavView —— Android右侧字母导航插件
![image](https://img.shields.io/badge/letterNavView-1.0.1-blue)
LetternavView是一个快速实现Android右侧字母导航的自定义View。
## 效果预览

## 使用步骤
---
Step1：在项目的build.gradle中引用
```gradle
implementation 'com.haitong:letter_nav_view:1.0.1'
```
Step2: 在布局文件中添加：

```xml
<com.haitong.letternavview.view.LetterNavView
        android:id="@+id/letterNavView"
        android:layout_width="22dp"
        android:layout_height="match_parent"
        android:layout_alignParentRight="true"
        app:navPaddingBottom="30dp"
        app:overlyTextSize="12sp" />
```
Step3: 设置监听

```java
letterNavView.setLetterChangeListener(new LetterNavView.LetterChangeListener() {
    @Override
    public void onTouchingLetterChanged(int index, String text) {
        // index为选中的下标， text为选中的文本
        // 做你要做的事吧
    }
});
```
## 属性值说明

Attributes | format| describe | defaultValue
---|---|---|---
navTvTextSize |integer | 导航栏字体大小|12sp
navTvTextSelectedSize | integer| 选中字体大小|12sp
navTvTextColor |color | 导航栏字体颜色|#8c8c8c
navTvTextSelectedColor | color| 选中字体颜色|#40c60a
navBgColor | color| 导航栏默认背景|Color.TRANSPARENT
navBgActiveColor | color| 导航栏激活状态（滑动时）背景|#40000000
navPaddingLeft |integer | 导航栏paddingLeft|0dp
navPaddingRight |integer | 导航栏paddingRight|0dp
navPaddingTop |integer | 导航栏paddingTop|10dp
navPaddingBottom | integer| 导航栏paddingBottom|10dp
overlyTvWidth |integer | 预览TextView宽度|65dp
overlyTvHeight |integer | 预览TextView高度|65dp
overlyTextSize | integer| 预览TextView字体大小|12sp
overlyTextColor | color| 预览TextView字体颜色|
overlyBgColor | color| 预览TextView背景颜色|#83000000
