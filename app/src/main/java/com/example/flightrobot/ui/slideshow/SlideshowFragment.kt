package com.example.flightrobot.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.example.flightrobot.R

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


        val items = listOf(
            BasicGridItem(R.drawable.ic_menu_camera, "One"),
            BasicGridItem(R.drawable.ic_menu_camera, "Two"),
            BasicGridItem(R.drawable.ic_menu_camera, "Three"),
            BasicGridItem(R.drawable.ic_menu_camera, "Four")
        )
/*
        jdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    cornerRadius(16f)
                    gridItems(items) { _, index, item ->
                        Toast.makeText(this.context, "\"Selected item ${item.title} at index $index\"", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }*/
        jdfix.setOnClickListener {
            this.context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.fix_title)
                    message(R.string.fix_mes)
                    positiveButton(R.string.agree) { dialog ->
                        // Do something
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