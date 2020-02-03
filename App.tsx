import React, {useState, useEffect} from 'react';
import { StyleSheet, Text, View } from 'react-native';
import WifiManager from './native/WifiManager';

export default function App() {
  WifiManager.log("Testing Test Render");

  const [info, setInfo] = useState("Loading");
  WifiManager.listWifi().then(info => setInfo(JSON.stringify(info))).catch((e) => setInfo(JSON.stringify(e)))

  useEffect(() => {
    WifiManager.checkPermission();
    WifiManager.scanWifi();
  }, [])

  return (
    <View style={styles.container}>
      <Text>Open up App.tsx to start working on your app!</Text>
      
  <Text>Hello World {info}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
