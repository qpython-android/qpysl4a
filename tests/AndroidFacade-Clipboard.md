# Testcase for setClipboard & getClipboard

## Test Result

- √ setClipboard
- √ getClipboard

## Test Script
```
# Test Code for setClipboard & getClipboard
# You can put some content into clipboard before running this test script and you will see the first makeToast shows the content

from androidhelper import Android
droid = Android()

clipboard = droid.getClipboard().result

droid.makeToast("clipboard: %s" % clipboard)

input(">Enter 1 continue\n")
#setClipboard
droid.setClipboard("Hello World")

#getClipboard
clipboard = droid.getClipboard().result

droid.makeToast("clipboard: %s" % clipboard)
```

## Test QrCode

[Open QRCode to Scan](http://qr.qpython.com.cn/?p=f1c788ec-360e-11ea-a0b0-fa163e575766)
