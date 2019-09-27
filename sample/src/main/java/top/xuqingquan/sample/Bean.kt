package top.xuqingquan.sample

data class Bean(val count:Int,val start:Int,val total:Int,val title:String,val subjects:List<Subjects>)

data class Subjects(val title:String)