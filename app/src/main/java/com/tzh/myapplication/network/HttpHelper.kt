package com.tzh.myapplication.network

import androidx.collection.ArrayMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tzh.myapplication.ui.dto.BaseResDto
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

object HttpHelper {

    /**
     * 一般一页多少条数据
     */
    const val PAGE_LIMIT_30 = 30
    const val PAGE_LIMIT_20 = 20
    const val PAGE_LIMIT_10 = 10

    /**
     * 是否打印头部日志
     */
    private var isShowHeadLogging = false

    /**
     * 是否打印身体日志
     */
    private var isShowBodyLogging = false

    fun isShowHeadLogging(): Boolean {
        return isShowHeadLogging
    }

    fun isShowBodyLogging(): Boolean {
        return isShowBodyLogging
    }


    /**
     * 存储 retrofit 的 接口实例
     *
     *
     * key-   包名.文件名
     *
     *
     * value- 接口类 实例
     */
    val mInterfaceMap = ArrayMap<String, Any>()

    /**
     * 存储  Retrofit 实例
     *
     *
     * key-  baseUrl
     *
     *
     * value- Retrofit 实例
     */
    val mRetrofitMap = ArrayMap<String, Retrofit>()

    /**
     * 绑定 retrofit 接口实例 Name 和 BaseUrl ;
     * 多个 接口实例 Name 可以对应一个 BaseUrl
     * key 接口实例 Name
     * value BaseUrl
     */
    val mInterfaceNameToBaseUrl = ArrayMap<String, String>()


    /**
     * 接口类 绑定  baseUrl
     *
     * @param baseUrl        当前请求的BaseUrl
     * @param interfaceClass 当前请求的 接口类
     * todo 当一个BaseUrl 只有几个 接口的时候，不建议 使用此方法，此方法会导致 Retrofit 实例 增加，并且不会被销毁
     * todo 每个 接口类 应当使用此方法
     */
    @Synchronized
    fun onBindingInterface(interfaceClass: Class<*>, baseUrl: String = BaseNetWork.baseUrl()) {
        // 新的 baseUrl 生成新的 retrofit 实例
        if (mRetrofitMap[baseUrl] == null) {
            mRetrofitMap[baseUrl] = BaseNetWork.getRetrofit(baseUrl)
        }
        if (mInterfaceNameToBaseUrl[interfaceClass.name] == null) {
            mInterfaceNameToBaseUrl[interfaceClass.name] = baseUrl
        }
        if (mRetrofitMap.size > 10) {
            throw RuntimeException("BaseHttpRequest Error: Retrofit 实例 不允许超过10个 ！")
        }
    }

    /**
     * 接口类 绑定  baseUrl
     *
     * @param baseUrl        当前请求的BaseUrl
     * @param interfaceClass 当前请求的 接口类
     * todo 当一个BaseUrl 只有几个 接口的时候，不建议 使用此方法，此方法会导致 Retrofit 实例 增加，并且不会被销毁
     * todo 每个 接口类 应当使用此方法
     */
    @Synchronized
    fun onBindingInterface(interfaceClass: Class<*>, retrofit: Retrofit, baseUrl: String = BaseNetWork.baseUrl()) {
        // 新的 baseUrl 生成新的 retrofit 实例
        if (mRetrofitMap[baseUrl] == null) {
            mRetrofitMap[baseUrl] = retrofit
        }
        if (mInterfaceNameToBaseUrl[interfaceClass.name] == null) {
            mInterfaceNameToBaseUrl[interfaceClass.name] = baseUrl
        }
        if (mRetrofitMap.size > 10) {
            throw RuntimeException("BaseHttpRequest Error: Retrofit 实例 不允许超过10个 ！")
        }
    }

    /**
     * 根据传入的 interface 自动生成 各个接口类的实例，且不会重复生成
     */
    @Synchronized
    inline fun <reified T> getInterfaceObject(interfaceClass: Class<T>): T {
        //当 interface 没有实例
        if (mInterfaceMap[interfaceClass.name] == null) {
            // 使用 interface Name 获取 baseUrl
            val baseUrl = mInterfaceNameToBaseUrl[interfaceClass.name] ?: throw RuntimeException("BaseHttpRequest Error: 接口类未初始化，请使用 onBindingBaseUrl 绑定 BaseUrl")
            // 使用 baseURL 获取 Retrofit 实例
            val retrofit = mRetrofitMap[baseUrl] ?: throw RuntimeException("BaseHttpRequest Error: Retrofit 未实例化，请使用 onBindingBaseUrl 初始化接口类！")
            mInterfaceMap[interfaceClass.name] = retrofit.create(interfaceClass)
        }
        if (mInterfaceMap[interfaceClass.name] !is T) {
            throw RuntimeException("BaseHttpRequest Error: Retrofit 未实例化，请使用 onBindingBaseUrl 初始化接口类！")
        }
        return mInterfaceMap[interfaceClass.name] as T
    }

    /**
     * RxJava 增加一个 onNext层，处理错误
     * 一般请求处理code不为0时的错误
     */
    fun <T : BaseResDto<*>> withDefault(observable: Observable<T>): Observable<T> {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(DefaultFailure())
    }

    /**
     * RxJava 增加一个 onNext层，处理错误
     * 一般请求处理code不为0时的错误
     */
    fun <T : BaseResDto<*>> withDefault(
        owner: LifecycleOwner,
        observable: Observable<T>
    ): ObservableSubscribeProxy<T> {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(DefaultFailure())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))
    }


    @JvmStatic
    fun <T> autoDisposable(owner: LifecycleOwner): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY))
    }
}

inline fun <reified T> xHttpRequest(): T {
    return HttpHelper.getInterfaceObject(T::class.java)
}

/**
 * 自动处理activity/fragment 生命周期
 */
fun <T : BaseResDto<*>> Observable<T>.xWithDefault(
    owner: LifecycleOwner,
    isRetryRequest: Boolean = false
): ObservableSubscribeProxy<T> {
    if (isRetryRequest) {
        //  this.retryWhen(RetryWithDelay(3, 3000))
    }
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(DefaultFailure())
        .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

}

/**
 * 需要手动 处理线程是否关闭
 */
fun <T : BaseResDto<*>> Observable<T>.xWithDefault(isRetryRequest: Boolean = false): Observable<T> {
    if (isRetryRequest) {
        //  this.retryWhen(RetryWithDelay(3, 3000))
    }
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(DefaultFailure())
}


/**
 * 自动处理activity/fragment 生命周期
 */
fun <T : BaseResDto<*>> Observable<T>.xWithLifecycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> {
    return `as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))

}