function port(url,body,id) {
            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200 || xhr.status === 304) {
                        let data = xhr.responseText;
                        document.getElementById(id).innerText = data;
                        let str  = ''+data;
                        let parse = JSON.parse(str);
                        alert(parse);
                    }
                }
            };
            xhr.open("POST", url, true);
            //如果是POST请求方式，设置请求首部信息
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.send("{'imageMd5':'"+body+"'}");
            document.getElementById(id).innerText = '请求中。。。';
}