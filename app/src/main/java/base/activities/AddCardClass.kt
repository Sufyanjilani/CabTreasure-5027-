package base.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import base.activities.Activity_HomePayment.REFERENCE
import base.utils.CommonVariables.JUDO_REGISTER_REQUEST
import base.utils.Config

object AddCardClass {
    //    public val RESUlTSUCCESS = PAYMENT_SUCCESS;
//    public val RESUlTCANCLLED = PAYMENT_CANCELLED;
//    val JUDORECEIPT = JUDO_RESULT;
//    var sp: SharedPreferences? = null
//
//    fun addCardRequest(context: Activity, sp: SharedPreferences) {
//
//        this.sp = sp;
//
//        val intent = Intent(context, JudoActivity::class.java);
//
//        intent.putExtra(
//            JUDO_OPTIONS,
//            getJudo(PaymentWidgetType.REGISTER_CARD, sp.getString(Config.JudoId, "")!!, false)
//        );
//
//        context.startActivityForResult(intent, JUDO_REGISTER_REQUEST);
//    }
//
//
//    @Throws(IllegalArgumentException::class, IllegalStateException::class)
//    private fun getJudo(widgetType: PaymentWidgetType, judoId: String, isSandboxed: Boolean): Judo {
//
//        val finaljudoId = judoId.replace("-", "")
//
//        return Judo.Builder(widgetType)
//            .setJudoId(finaljudoId)
//            .setAuthorization(authorization)
//            .setAmount(amount)
//            .setReference(reference)
//            .setIsSandboxed(isSandboxed)
//            .build()
//    }
//
//    private val amount: Amount
//        get() {
//            return Amount.Builder()
//                .setCurrency(Currency.GBP)
//                .build()
//        }
//
//
//    private val reference: Reference
//        get() {
//            return Reference.Builder()
//                .setConsumerReference(REFERENCE)
//                .build()
//        }
//
//    private val authorization: Authorization
//        get() {
//            val token = sp?.getString(Config.JudoToken, null)
//            val secret = sp?.getString(Config.JudoSecret, null)
//
//
//            return BasicAuthorization.Builder()
//                .setApiToken(token)
//                .setApiSecret(secret)
//                .build()
//        }
}