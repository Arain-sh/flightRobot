package com.example.flightrobot.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.example.flightrobot.ActionRecyclerAdapter
import com.example.flightrobot.OperationRecyclerAdapter
import com.example.flightrobot.R
import com.example.flightrobot.models.airinfoResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_operation.*
import kotlinx.android.synthetic.main.fragment_taskinfo.*
import kotlinx.android.synthetic.main.fragment_taskinfo.view.*
import rxhttp.RxHttp

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        val jdfix: Button = root.findViewById(R.id.jd_button)
        val wdfix: Button = root.findViewById(R.id.wd_button)
        val gdfix: Button =root.findViewById(R.id.gd_button)
        val yxgdfix: Button = root.findViewById(R.id.yxgd_button)
        val sdfix: Button = root.findViewById(R.id.sd_button)
        val yxsdfix: Button = root.findViewById(R.id.yxsd_button)
        val czsdfix: Button = root.findViewById(R.id.czsd_button)
        val yxczsdfix: Button = root.findViewById(R.id.yxczsd_button)
        val hxfix: Button = root.findViewById(R.id.hx_button)
        val yxhxfix: Button = root.findViewById(R.id.yxhx_button)
        val wxdgdfix: Button = root.findViewById(R.id.wxdgd_button)
        val mhsfix: Button = root.findViewById(R.id.mhs_button)
        val pljfix: Button = root.findViewById(R.id.plj_button)
        val gjfix: Button = root.findViewById(R.id.gj_button)
        val chjfix: Button = root.findViewById(R.id.chj_button)
        val fdjzsfix: Button = root.findViewById(R.id.fdjzs_button)
        val fdjpqwdfix: Button = root.findViewById(R.id.fdjpqwd_button)
        val fdjymjdfix: Button = root.findViewById(R.id.fdjymjd_button)
        val fdjnjfix: Button = root.findViewById(R.id.fdjnj_button)
        val bjhgjfix: Button = root.findViewById(R.id.bjhgj_button)
        val bjfyjfix: Button = root.findViewById(R.id.bjfyj_button)
        val bjzyhgjfix: Button = root.findViewById(R.id.bjzyhgj_button)
        val bjzyfyjfix: Button = root.findViewById(R.id.bjzyfyj_button)

        // edittext
        val jdEdit: EditText = root.findViewById(R.id.inf_jd)
        val wdEdit: EditText = root.findViewById(R.id.inf_wd)
        val gdEdit: EditText = root.findViewById(R.id.inf_gd)
        val yxgdEdit: EditText = root.findViewById(R.id.inf_yxgd)
        val sdEdit: EditText = root.findViewById(R.id.inf_sd)
        val yxsdEdit: EditText = root.findViewById(R.id.inf_yxsd)
        val czsdEdit: EditText = root.findViewById(R.id.inf_czsd)
        val yxczsdEdit: EditText = root.findViewById(R.id.inf_yxczsd)
        val hxEdit: EditText = root.findViewById(R.id.inf_hx)
        val yxhxEdit: EditText = root.findViewById(R.id.inf_yxhx)
        val wxdgdEdit: EditText = root.findViewById(R.id.inf_wxdgd)
        val mhsEdit: EditText = root.findViewById(R.id.inf_mhs)
        val pljEdit: EditText = root.findViewById(R.id.inf_plj)
        val gjEdit: EditText = root.findViewById(R.id.inf_gj)
        val chjEdit: EditText = root.findViewById(R.id.inf_chj)
        val fdjzsEdit: EditText = root.findViewById(R.id.inf_fdjzs)
        val fdjpqwdEdit: EditText = root.findViewById(R.id.inf_fdjpqwd)
        val fdjymjdEdit: EditText = root.findViewById(R.id.inf_fdjymjd)
        val fdjnjEdit: EditText = root.findViewById(R.id.inf_fdjnj)
        val bjhgjEdit: EditText = root.findViewById(R.id.inf_bjhgj)
        val bjfyjEdit: EditText = root.findViewById(R.id.inf_bjfyj)
        val bjzyhgjEdit: EditText = root.findViewById(R.id.inf_bjzyhgj)
        val bjzyfyjEdit: EditText = root.findViewById(R.id.inf_bjzyfyj)




        val items = listOf(
            BasicGridItem(R.drawable.ic_menu_camera, "One"),
            BasicGridItem(R.drawable.ic_menu_camera, "Two"),
            BasicGridItem(R.drawable.ic_menu_camera, "Three"),
            BasicGridItem(R.drawable.ic_menu_camera, "Four")
        )
        //????????????
        // kotlin
        RxHttp.get(this.getString(R.string.default_url) + "/api/v1/airinfos")
            .asString()
            .subscribe({ s ->
                try {
                    var s: airinfoResponse = Gson().fromJson(s, airinfoResponse::class.java)

                    var airinfos = s.data.get(0)

                    airinfos?.let{
                        jdEdit.hint = airinfos.jd.plus(" ??").toString()
                        wdEdit.hint = airinfos.wd.plus(" ??").toString()
                        gdEdit.hint = (airinfos.gd.plus(" m")).toString()
                        yxgdEdit.hint = (airinfos.yxgd.plus("m")).toString()
                        sdEdit.hint = airinfos.sd.plus(" m/s").toString()
                        yxsdEdit.hint = airinfos.yxsd.plus(" m/s").toString()
                        czsdEdit.hint = airinfos.czsd.plus(" m/s").toString()
                        yxczsdEdit.hint = airinfos.yxczsd.plus(" m/s").toString()
                        hxEdit.hint = airinfos.hx.plus(" ??").toString()
                        yxhxEdit.hint = airinfos.yxhx.plus(" ??").toString()
                        wxdgdEdit.hint = airinfos.wxdgd.plus(" m").toString()
                        mhsEdit.hint = airinfos.mhs
                        pljEdit.hint = airinfos.plj.plus(" ??").toString()
                        gjEdit.hint = airinfos.gj.plus(" ??").toString()
                        chjEdit.hint = airinfos.chj.plus(" ??").toString()
                        fdjzsEdit.hint = airinfos.fdjzs
                        fdjpqwdEdit.hint = airinfos.fdjpqwd.plus(" ??C").toString()
                        fdjymjdEdit.hint = airinfos.fdjymjd.plus(" ??").toString()
                        fdjnjEdit.hint = airinfos.fdjnj.plus(" ??").toString()
                        bjhgjEdit.hint = airinfos.bjhgj.plus(" ??").toString()
                        bjfyjEdit.hint = airinfos.bjfyj.plus(" ??").toString()
                        bjzyhgjEdit.hint = airinfos.bjzyhgj.plus(" ??").toString()
                        bjzyfyjEdit.hint = airinfos.bjzyfyj.plus(" ??").toString()
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }, { throwable ->
                println(throwable)
                println("Sys Log: cannot get data")
            })

        jdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        // Do something
                        // kotlin
                        jdEdit.clearFocus()
                        RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/airinfos")
                            .add("id", 1)
                            .add("jd", jdEdit.text)
                            .asString()
                            .subscribe({ s ->
                                try {

                                } catch (e: Exception) {
                                    println(e)
                                }
                            }, { throwable ->
                                println(throwable)
                                println("Sys Log: cannot get data")
                            })
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        wdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        // Do something
                        wdEdit.clearFocus()

                        RxHttp.postForm(requireParentFragment().getString(R.string.default_url) + "/api/v1/airinfos")
                            .add("id", 1)
                            .add("wd", wdEdit.text)
                            .asString()
                            .subscribe({ s ->
                                try {

                                } catch (e: Exception) {
                                    println(e)
                                }
                            }, { throwable ->
                                println(throwable)
                                println("Sys Log: cannot get data")
                            })
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        gdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        gdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        yxgdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        yxgdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        sdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        sdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        yxsdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        yxsdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        czsdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        czsdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        yxczsdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        yxczsdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        hxfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        hxEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        yxhxfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        yxhxEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        wxdgdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        wxdgdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        mhsfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        mhsEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        pljfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        pljEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        gjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        gjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        chjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        chjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        fdjzsfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        fdjzsEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        fdjpqwdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        fdjpqwdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        fdjymjdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        fdjymjdEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        fdjnjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        fdjnjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        bjhgjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        bjhgjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        bjfyjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        bjfyjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        bjzyhgjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        bjzyhgjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }

        bjzyfyjfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        bjzyfyjEdit.clearFocus()
                        // Do something
                    }
                    negativeButton(R.string.disagree) { dialog ->
                        // Do something
                    }
                }
            }
        }


        return root
    }
}