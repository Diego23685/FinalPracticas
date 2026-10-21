export interface IInsumo {
  id?: number;
  nombre?: string;
  codigo?: string;
  stock?: number;
  precioUnitario?: number;
}

export const defaultValue: Readonly<IInsumo> = {};
