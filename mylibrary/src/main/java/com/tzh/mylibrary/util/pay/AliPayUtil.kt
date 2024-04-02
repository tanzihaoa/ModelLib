package com.tzh.mylibrary.util.pay

import android.app.Activity
import android.os.AsyncTask
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alipay.sdk.app.PayTask
import com.tzh.mylibrary.livedata.PayRefreshData

object AliPayUtil{
    fun startAliPay(activity : AppCompatActivity, payDto : String){
        AliPayTask(activity).execute(payDto)
    }

    private class AliPayTask(val mActivity: Activity) :
        AsyncTask<String?, Void?, String?>() {
        override fun onPostExecute(errorCode: String?) {
            if ("9000" == errorCode) {
                PayRefreshData.instance.setValue(PayRefreshEvent(true, PaymentType.aliPay, errorCode, ""))
            } else if ("8000" == errorCode) {
                PayRefreshData.instance.setValue(PayRefreshEvent(false, PaymentType.aliPay, errorCode, ""))
            } else if ("6001" == errorCode) {
                PayRefreshData.instance.setValue(PayRefreshEvent(false, PaymentType.aliPay, errorCode, ""))
                Toast.makeText(mActivity,"已取消支付",Toast.LENGTH_LONG).show()
            } else {
                PayRefreshData.instance.setValue(PayRefreshEvent(false, PaymentType.aliPay, errorCode!!, ""))
                Toast.makeText(mActivity,"支付失败，错误码：%s",Toast.LENGTH_LONG).show()
            }
        }

        override fun doInBackground(vararg params: String?): String?{
            // 调用支付接口，获取支付结果
            val result = PayTask(mActivity).pay(params[0], false)
            if (StringUtils.isValid(result)) {
                var resultStatus: String? = null
                val resultParams = result.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                for (resultParam in resultParams) {
                    if (resultParam.startsWith("resultStatus")) {
                        val prefix = "resultStatus={"
                        resultStatus = resultParam.substring(
                            resultParam.indexOf(prefix) + prefix.length,
                            resultParam.lastIndexOf("}")
                        )
                        break
                    }
                }
                return resultStatus
            }
            return "-1"
        }
    }
}