import { useEffect, useState } from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';
import { addPiPModeChangedListener, enterPiP } from 'react-native-nitro-pip';

export default function App() {
  const [status, setStatus] = useState<string>('Ready');

  useEffect(() => {
    const unsubscribe = addPiPModeChangedListener((isInPiP) => {
      setStatus(isInPiP ? 'PiP mode active' : 'PiP mode exited');
    });

    return unsubscribe;
  }, []);

  const handleEnterPiP = () => {
    const result = enterPiP();
    setStatus(result ? 'Entered PiP mode' : 'Failed to enter PiP');
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>PiP Module Demo</Text>
      <Button title="Enter PiP Mode" onPress={handleEnterPiP} />
      <Text style={styles.status}>{status}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  status: {
    marginTop: 20,
    fontSize: 16,
    color: '#666',
  },
});
