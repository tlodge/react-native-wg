import { NativeModules } from 'react-native';

type WgType = {
  multiply(a: number, b: number): Promise<number>;
  connect(a:string, b:string, c:string, d:string, e:string): Promise<string>;
};

const { Wg } = NativeModules;

export default Wg as WgType;
