# JavaPOS 

## 如何編譯
1. 編寫MANIFEST.TXT檔	
2. javac -encoding "UTF-8" One.java
3. jar -cvfm "ANY_NAME".jar MANIFEST.txt ./
4. java -jar "ANY_NAME".jar

## Raspbian下如何讓D-WAV Scientifict 觸控螢幕能運作
1. 下載 evdev 驅動程式
* sudo apt-get install xserver-xorg-input-evdev
2. 修改/usr/share/X11/xorg.conf.d/40-libinput.conf
* 新增下列片段
```
Section "InputClass"
    Identifier "evdev tablet catchall"
    MatchIsTablet "on"
    MatchDevicePath "/dev/input/event*"
    Driver "evdev"
EndSection
```
## v2.4.0
1. 列印歡迎詞於結尾

## v2.3.0
1. OutputPrinter.java 修改資料結構，以正確存取資料
2. 列印版面

## v2.2.0
1. 為了raspberry pi 顯示修改元件尺寸

