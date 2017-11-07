var ip='ip:port'
var uploader = new plupload.Uploader({
    runtimes : 'html5,flash,silverlight',//设置运行环境，会按设置的顺序，可以选择的值有html5,gears,flash,silverlight,browserplus,html
    flash_swf_url : '/js/plupload/Moxie.swf',
    silverlight_xap_url : '/js/plupload/Moxie.xap',
    url : '/moniterb/savefileb',//上传文件路径配合multipart是false
    //url : 'http://192.168.20.41:8080/moniter/savefile',//上传文件路径配合multipart是true
    max_file_size : '200kb',//100b, 10kb, 10mb, 1gb
    browse_button : 'selectfiles',
    multi_selection: false,//多选设置
    multipart:false,//为true以multipart/form-data的形式来上传文件，为false以二进制的格式来上传文件
    filters : {
        mime_types : [ //只允许上传图片和mp3文件
            { title : "文件类型", extensions : "xls" }
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
                    document.getElementById('hyupfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                        +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                        +'</div>';
            });
        },


        FileUploaded : function(up, file, info) {//文件上传完毕触发
            var response = $.parseJSON(info.response);
            if (response.status=="success") {
                document.getElementById('console').appendChild(document.createTextNode(info.response));
                document.getElementById('fileuri').value= '/moniter/readfileNew?info='+response.fileinfo;
                document.getElementById('downloadfile').setAttribute('href','/moniter/downloadExcelFile?info='+response.fileinfo);
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

uploader.init();
