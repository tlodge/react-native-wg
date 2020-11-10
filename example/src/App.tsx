import * as React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import Wg from 'react-native-wg';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const [connection, setConnection] = React.useState<string | undefined>();
  React.useEffect(() => {
    Wg.multiply(3, 7).then(setResult);
  }, []);

  React.useEffect(() => {
    Wg.connect("a","b", "c", "d", "e").then(setConnection);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result} {connection}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
