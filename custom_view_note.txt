
paint 的方法

//设置一个渲染器  SweepGradient角度渲染  LinearGradient线性渲染 RadialGradient中心扩散渲染 BitmapShader ComposeShader

paint.setShader();

//设置ColorFilter  

   LightingColorFilter(int mul,int add); mul 与目标像素相乘,add 与目标像素相加, 参数都是颜色的int值一致.最基本的参数为 mul = 0xffffff  add = 0x000000  这个时候设置上去 是没有任何效果的. 如果想去掉红色 让 mul = 0x00ffff  也就是红色的值改为0  add保持不变就可以 
   
   计算过程为 : R' =  R * 0x00 / 0xff + 0x00 = 0;
               G' =  G * 0xff / 0xff + 0x00 = G;
               B' =  B * 0xff / 0xff + 0x00 = B;
               
想要加强指定的颜色值  就可以 设置mul 为 0xffffff  然后让add 中指定的数值增大 比如加强绿色  add = 0x003300;  这样  绿色会被增强 其它的颜色保持不变

paint.setColorFilter();
