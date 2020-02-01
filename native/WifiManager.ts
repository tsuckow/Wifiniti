interface WifiInfo {
  SSID: string;
  BSSID: string;
  capabilities: string;
  frequency: string;
  level: string;
  timestamp: string;
}

interface WifiManager {
  /**
   * Logs a messages
   */
  log: (message: string) => void;

  listWifi: () => Promise<WifiInfo[]>
}

import { NativeModules } from "react-native";
export default NativeModules.WifiManager as WifiManager;
