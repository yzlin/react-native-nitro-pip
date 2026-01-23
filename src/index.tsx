import { NitroModules } from 'react-native-nitro-modules';
import type { NitroPip } from './NitroPip.nitro';

const NitroPipHybridObject =
  NitroModules.createHybridObject<NitroPip>('NitroPip');

export function multiply(a: number, b: number): number {
  return NitroPipHybridObject.multiply(a, b);
}
