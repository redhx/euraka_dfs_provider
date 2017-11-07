var uploader = new plupload.Uploader({
    runtimes : 'html5,flash,silverlight',//设置运行环境，会按设置的顺序，可以选择的值有html5,gears,flash,silverlight,browserplus,html
    flash_swf_url : '/js/plupload/Moxie.swf',
    silverlight_xap_url : '/js/plupload/Moxie.xap',
    url : 'http://192.168.18.120:8083/moniterb/savefileb',//上传文件路径配合multipart是false
    //url : 'http://192.168.20.41:8080/moniter/savefile',//上传文件路径配合multipart是true
    max_file_size : '200kb',//100b, 10kb, 10mb, 1gb
    browse_button : 'selectfiles',
    multi_selection: false,//多选设置
    multipart:false,//为true以multipart/form-data的形式来上传文件，为false以二进制的格式来上传文件
    filters : {
        mime_types : [ //只允许上传图片和mp3文件
            { title : "图片类型", extensions : "jpg,gif" }
        ],
        max_file_size : '300kb', //最大只能上传300kb的文件
        prevent_duplicates : true //不允许选取重复文件
    },

    init : {

        PostInit: function() {
            document.getElementById('postfiles').onclick = function() {
                uploader.start();
                return false;
            };
        },


        FilesAdded: function(up, files) {
            plupload.each(files, function(file) {
                previewImage(file, function (file,imgsrc) {
                    document.getElementById('hyupfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                        +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                        +'<div> <img width="100" height="100" src="' + imgsrc + '" name="' + file.name + '" /> </div>'
                        +'</div>';
                });
            });
        },


        FileUploaded : function(up, file, info) {//文件上传完毕触发
            var response = $.parseJSON(info.response);
            if (response.status=="success") {
                document.getElementById('console').appendChild(document.createTextNode(info.response));
                document.getElementById('fileuri').value= 'http://192.168.18.120:8083/readfileNew?info='+response.fileinfo;
                document.getElementById('showimg').src= 'http://192.168.18.120:8083/readfileNew?info='+response.fileinfo;
            }else{
                document.getElementById('console').appendChild(document.createTextNode(info.response));
            }
        },


        UploadComplete : function( uploader,files ) {
            console.log("所有文件上传完毕");
        },


        UploadProgress : function( uploader,file ) {
            var d = document.getElementById(file.id);
            d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            var prog = d.getElementsByTagName('div')[0];
            var progBar = prog.getElementsByTagName('div')[0]
            progBar.style.width= 2*file.percent+'px';
            progBar.setAttribute('aria-valuenow', file.percent);
        },

        Error: function(up, err) {
            document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
        }

    }
});

//plupload中为我们提供了mOxie对象
//有关mOxie的介绍和说明请看：https://github.com/moxiecode/moxie/wiki/API
//如果你不想了解那么多的话，那就照抄本示例的代码来得到预览的图片吧
function previewImage(file, callback) {//file为plupload事件监听函数参数中的file对象,callback为预览图片准备完成的回调函数
    if (!file || !/image\//.test(file.type)) return; //确保文件是图片
    if (file.type == 'image/gif') {//gif使用FileReader进行预览,因为mOxie.Image只支持jpg和png
        var fr = new mOxie.FileReader();
        fr.onload = function () {
            callback(file,fr.result);
            fr.destroy();
            fr = null;
        }
        fr.readAsDataURL(file.getSource());
    } else {
        var preloader = new mOxie.Image();
        preloader.onload = function () {
            //preloader.downsize(550, 400);//先压缩一下要预览的图片,宽300，高300
            var imgsrc = preloader.type == 'image/jpeg' ? preloader.getAsDataURL('image/jpeg', 80) : preloader.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
            callback && callback(file,imgsrc); //callback传入的参数为预览图片的url
            preloader.destroy();
            preloader = null;
        };
        preloader.load(file.getSource());
    }
}

uploader.init();
