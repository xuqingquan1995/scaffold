package top.xuqingquan.stack

import androidx.fragment.app.Fragment

/**
 * Created by 许清泉 on 2019-05-07 22:04
 */
object StackList {
    private val tempList: MutableList<Map<String, Fragment>> = arrayListOf()
    var instance: MutableList<DebugFragmentRecord>? = null
        private set
        get() {
            if (field == null) {
                synchronized(this) {
                    if (field == null) {
                        field = arrayListOf()
                    }
                }
            }
            return field!!
        }

    fun addFragmet(fragment: Fragment) {
        val tag = createTag(fragment)
        val map = HashMap<String, Fragment>()
        map[tag] = fragment
        if (tempList.contains(map)) {
            removeFragment(fragment)
            tempList.remove(map)
        }
        tempList.add(map)
        val list = instance!!
        val parentFragment = fragment.parentFragment
        if (list.isEmpty() || parentFragment == null) {//顶层
            list.add(createDebugFragmentRecord(fragment))
        } else {//之后的fragment
            list.forEach {
                val name = simpleName(parentFragment)//父fragment
                val item = it.fragmentName//遍历的fragment
                if (name == item) {
                    it.childFragmentRecord!!.add(0, createDebugFragmentRecord(fragment))
                } else {
                    addChildFragment(fragment, it)
                }
            }
        }
        instance = list
    }

    private fun addChildFragment(fragment: Fragment, record: DebugFragmentRecord) {
        record.childFragmentRecord!!.forEach {
            if (it.fragmentName == simpleName(fragment.parentFragment!!)) {
                it.childFragmentRecord!!.add(0, createDebugFragmentRecord(fragment))
            } else {
                addChildFragment(fragment, it)
            }
        }
    }

    fun removeFragment(fragment: Fragment) {
        val tag = createTag(fragment)
        val map = HashMap<String, Fragment>()
        map[tag] = fragment
        if (tempList.contains(map)) {
            tempList.remove(map)
        }
        for (it in instance!!) {
            if (it.tag == tag) {
                instance!!.remove(it)
                break
            } else {
                removeChild(fragment, it)
            }
        }
    }

    private fun removeChild(fragment: Fragment, record: DebugFragmentRecord) {
        val fragmentRecord = record.childFragmentRecord
        for (it in fragmentRecord!!) {
            if (it.tag == createTag(fragment)) {
                fragmentRecord.remove(it)
                break
            } else {
                removeChild(fragment, it)
            }
        }
    }

    private fun createDebugFragmentRecord(fragment: Fragment) = DebugFragmentRecord(
        simpleName(fragment),
        arrayListOf(),
        createTag(fragment)
    )

    private fun createTag(fragment: Fragment): String {
        val sb = StringBuilder()
        val name = name(fragment)
        val parentFragment = fragment.parentFragment
        return if (parentFragment != null) {
            sb.append(createTag(sb, fragment))
            if (!sb.toString().endsWith(name)) {
                sb.append("-->").append(name)
            }
            sb
        } else {
            sb.append(name)
        }.toString()
    }

    private fun createTag(sb: StringBuilder, fragment: Fragment): StringBuilder {
        val ss = StringBuilder()
        val parentFragment = fragment.parentFragment
        ss.append(name(fragment))
        return if (parentFragment != null) {
            createTag(ss, parentFragment)
        } else {
            ss.append("-->").append(sb)
        }
    }

    private fun name(fragment: Fragment) = fragment::class.java.name

    private fun simpleName(fragment: Fragment) = fragment::class.java.simpleName

}