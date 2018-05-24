## Editor.md 的使用

#### 1. 从官网下载相应的组件

![editorMd](https://upload-images.jianshu.io/upload_images/2192622-326cbf172ea947e0.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/613)



#### 2. 在页面中使用

```html
<div id="my-editormd">
            <textarea id="editormd-md-textarea" name="doc"
                      style="display:none;"></textarea>
            <!-- 第二个隐藏文本域，用来构造生成的HTML代码，方便表单POST提交，这里的name可以任意取，后台接受时以这个name键为准 -->
            <textarea id="editormd-html-textarea" name="html"></textarea>
</div>


<script type="text/javascript">
    var testEditor;

    $(function () {
        testEditor = editormd("my-editormd", {
            width: "90%",
            height: 640,
            syncScrolling: "single",
            path: "../lib/",
           //这个配置在simple.html中并没有，但是为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
            saveHTMLToTextarea: true,

            imageUpload: true,
            imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
            imageUploadURL : "/uploadImage",//注意你后端的上传图片服务地址
          	onload: function(){
                this.width("100%");
                this.height(480);
            }
        });
    });
</script>

var content = $('.editormd-markdown-textarea').val(); 获取值
```

* 后端接受图片

```java
    //发布项目后改为项目地址
    private static String UPLOADED_FOLDER = "F:\\ideaTest\\gakki\\src\\main\\webapp\\image\\";

    @RequestMapping("/uploadimg")
    public Map<String, Object> editormdPic(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String fileName = file.getOriginalFilename();// 获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));// 获取文件的后缀
        String newFileName = DateUtil.getCurrentDateStr() + suffixName;
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File(UPLOADED_FOLDER + newFileName));
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url", "http://localhost:8888/image/" + newFileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }

        return resultMap;
    }
```

前段显示

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>md</title>
    <link rel="stylesheet" href="editormd.preview.css" />
    <script src="/static/jquery.min.js"></script>
    <script src="/lib/marked.min.js"></script>
    <script src="/lib/prettify.min.js"></script>
    <script src="/lib/raphael.min.js"></script>
    <script src="/lib/underscore.min.js"></script>
    <script src="/lib/sequence-diagram.min.js"></script>
    <script src="/lib/flowchart.min.js"></script>
    <script src="/lib/jquery.flowchart.min.js"></script>
    <script src="editormd.min.js"></script>
</head>
<body>
<div id="doc-content">
    <textarea style="display:none;" placeholder="markdown语言">${md.content }</textarea>
</div>

<script type="text/javascript">
    var testEditor;
    $(function () {
        testEditor = editormd.markdownToHTML("doc-content", {//注意：这里是上面DIV的id
            htmlDecode: "style,script,iframe",
            emoji: true,
            taskList: true,
            tocm: true,
            tex: true, // 默认不解析
            flowChart: true, // 默认不解析
            sequenceDiagram: true, // 默认不解析
            codeFold: true
        });});
</script>
</body>
</html>
```

