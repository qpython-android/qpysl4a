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

<img src='http://qr.qpython.com.cn/assets/codes/q-127.0.0.1-ab751ec0-3606-11ea-a0b0-fa163e575766.png' />
