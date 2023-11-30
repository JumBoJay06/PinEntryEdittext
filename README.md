# PinEntryEditText
<img width="300" height="480" src="/pic/Screenshot.png"/>

# How to use
首先，把class `PinEntryEditText` 複製到自己的專案  
然後再自己專案的`style.xml`下新增  
```xml
    <declare-styleable name="PinEntryEditText">
        <attr name="editTextBackground" format="reference"/>
        <attr name="editTextSpace" format="dimension"/>
    </declare-styleable>
```
此View與一般EditText用法差不多，不過需加上一些屬性，主要目的是限制輸入格式、隱藏光標與阻擋反白
```sh
    android:cursorVisible="false"
    android:digits="1234567890"
    android:inputType="number"
    android:textIsSelectable="false"
```  
----  
字數限制用以下屬性控制
```sh
    android:maxLength="5"
``` 
----  
每格的間距用以下屬性(此屬性是自定義屬性，不可以`android:`開頭)
```sh
    app:editTextSpace="12dp"
```  
----  
每格的背景可以更換，目前是以`ResourceId`轉換成`drawable`當作背景(此屬性是自定義屬性，不可以`android:`開頭)  
若無設定，每格會以底線顯示(如附圖第三個樣式)  
```sh
    app:editTextBackground="@mipmap/ic_launcher_round"
```  
or  
```sh
    app:editTextBackground="@drawable/edit_text_border"
```  
or 也可以動態更換
```kotlin
    edittext.setEditBlockBackground(@DrawableRes resourcesId)
```
----  
文字顏色字體等，直接使用原本EditText的屬性設定，另外此View無實作任何動畫