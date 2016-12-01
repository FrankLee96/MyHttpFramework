# MyHttpFramework
## 简介
一个类似Volley的网络框架，用于自己学习使用，以Request任务队列为主体，网络执行使用HttpURLConnection。
## 更新
### 1.0
完成主体部分
### 1.1
加入了缓存的使用
### 1.2
加入大文件上传支持（BigMultipartRequest），待测试
### 1.3
加入了使用OkHttp的网络执行栈OkHttpStack;  
更改了Response的实现方式， 减少了对Client包的依赖（因为HttpClient已经被废弃）
