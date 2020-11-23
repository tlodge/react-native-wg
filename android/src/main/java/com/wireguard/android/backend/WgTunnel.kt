package com.wireguard.android.backend;

import android.util.Log;
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WgTunnel internal constructor(
  private var name: String,
  state: Tunnel.State
) :  Tunnel {

  override fun getName() = name;

  var state = state
    private set

  override fun onStateChange(newState: Tunnel.State) {
    onStateChanged(newState)
  }

  fun onStateChanged(state: Tunnel.State): Tunnel.State {
    this.state = state;
    return state
  }

  /*suspend fun setStateAsync(state: Tunnel.State): Tunnel.State = withContext(Dispatchers.Main.immediate) {
      if (state != this@WgTunnel.state)
          state = withContext(Dispatchers.IO) { getBackend().setState(tunnel, state, tunnel.getConfigAsync()) }
          this@WgTunnel.state = state
      else
          this@WgTunnel.state
  }*/

  companion object {
    private const val TAG = "WireGuard/WgTunnel"
  }
}
