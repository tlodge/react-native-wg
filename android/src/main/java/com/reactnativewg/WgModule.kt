package com.reactnativewg

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.wireguard.android.backend.Backend
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import com.wireguard.android.backend.WgTunnel
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.wireguard.config.Config
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import androidx.lifecycle.lifecycleScope
import android.app.Activity;
import android.content.Intent;
import kotlinx.coroutines.*
class WgModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener  {

  init {
    reactApplicationContext.addActivityEventListener(this)
  }

  private var configText: String = "[Interface]\nAddress = 10.200.100.8/24\n DNS = 10.200.100.1\n PrivateKey = oK56DE9Ue9zK76rAc8pBl6opph+1v36lm7cXXsQKrQM=\n" +
    "\n" +
    "[Peer]\nPublicKey = GtL7fZc/bLnqZldpVofMCD6hDjrK28SsdLxevJ+qtKU=\n PresharedKey = /UwcSPg38hW/D9Y3tcS1FOV0K1wuURMbS0sesJEP5ak=\nAllowedIPs = 0.0.0.0/0\nEndpoint = demo.wireguard.com:51820\n"

  override fun getName(): String {
    return "Wg"
  }

  private var tunnel: WgTunnel?  = null
  private var backend: GoBackend? = null
  //private val ta: AppCompatActivity? = null


  // Example method
  // See https://facebook.github.io/react-native/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Int, b: Int, promise: Promise) {
    promise.resolve(a * b)
  }

  @ReactMethod
  fun connect(confStr: String, session: String, icon: String, title: String, text: String, promise: Promise){

    Log.d("ReactNative", "creating the backend!")
    backend = GoBackend(reactApplicationContext)
    tunnel = WgTunnel("mytunnel", Tunnel.State.DOWN)

    val intent = GoBackend.VpnService.prepare(reactApplicationContext)

    if (intent != null) {
      Log.d("ReactNative", "prepared vpn service!!!!");
    }


    //here we need to create a new activity, which will then start the vpn service using
    //start activity for result (to ask for permssions), and which will then continue
    //to set up the tunnel!

    if (intent != null) {
      //here we need to ask for permission from the user...?
      Log.d("ReactNative", "----------------- neeed to ask for vpn permissions -------------------");
      //tunnel = WgTunnel ("mytunnel", Tunnel.State.DOWN)
      reactApplicationContext.startActivityForResult(intent, 1, null);

      //permissionActivityResultLauncher.launch(intent)
      //return@launch
    }else{
      Log.d("ReactNative", "done!, SO NOW CREATING BACKEND STATE");
      backend?.setState(tunnel, Tunnel.State.TOGGLE, Config.parse(ByteArrayInputStream(configText.toByteArray(StandardCharsets.UTF_8))));
      Log.d("ReactNative", "*** DONE!");
    }
    //tunnel = WgTunnel("mytunnel", Tunnel.State.DOWN);
    //try{
    //Config.parse(ByteArrayInputStream(configText.toByteArray(StandardCharsets.UTF_8)));
    //    Log.d("ReactNative", "done!, SO NOW CREATING BACKEND STATE");
    //    backend.setState(tunnel, Tunnel.State.TOGGLE, Config.parse(ByteArrayInputStream(configText.toByteArray(StandardCharsets.UTF_8))));
    //    Log.d("ReactNative", "*** DONE!");
    //}catch (e: Exception) {
    //    Log.d("ReactNative", e.toString());
    //}


    Log.d("ReactNative", "am here!");
    promise.resolve("seen a connection!!" + confStr + session + icon + title + text);
  }

  @Override
  override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {

    Log.d("ReactNative", "boom - have a result!!");

    try{
      Log.d("ReactNative", "done!, SO NOW CREATING BACKEND STATE");
      GlobalScope.launch {
        backend?.setState(tunnel, Tunnel.State.TOGGLE, Config.parse(ByteArrayInputStream(configText.toByteArray(StandardCharsets.UTF_8))));
      }

      Log.d("ReactNative", "*** DONE!");
    }catch (e: Exception) {
      Log.d("ReactNative", e.toString());
    }
  }

  @Override
  override fun onNewIntent(intent: Intent?) {
  }

}
