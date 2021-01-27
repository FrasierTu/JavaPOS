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
		

