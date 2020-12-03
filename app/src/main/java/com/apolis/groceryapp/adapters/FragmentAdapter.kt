package com.apolis.groceryapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.apolis.groceryapp.fragments.SubcategoryFragment
import com.apolis.groceryapp.models.Category
import com.apolis.groceryapp.models.Subcategory

class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){

    var mFragmentList: ArrayList<Fragment> = ArrayList()
    var mTitleList: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }

    // A custom method to add fragments and titles to appropriate lists
    fun addSubcategoryFragments(subcategory: Subcategory) {
        mFragmentList.add(SubcategoryFragment.newInstance(subcategory.subId))
        mTitleList.add(subcategory.subName)
    }

}