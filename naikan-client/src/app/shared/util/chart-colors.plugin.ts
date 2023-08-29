import {Chart, ChartDataset, DoughnutController, PolarAreaController} from "chart.js";

export interface ColorsPluginOptions {
  enabled?: boolean;
  forceOverride?: boolean;
}

interface ColorsDescriptor {
  backgroundColor?: any;
  borderColor?: any;
}

const BORDER_COLORS: string[] = [
  '#0f4880', '#0f488080', '#0f488060', '#0f488040', '#0f488020',
  '#007E33', '#007E3380', '#007E3360', '#007E3340', '#007E3320',
  '#5d0c28', '#5d0c2880', '#5d0c2860', '#5d0c2840', '#5d0c2820'
];

const BACKGROUND_COLORS: string[] = BORDER_COLORS;

function getBorderColor(i: number): string {
  return BORDER_COLORS[i % BORDER_COLORS.length];
}

function getBackgroundColor(i: number): string {
  return BACKGROUND_COLORS[i % BACKGROUND_COLORS.length];
}

function colorizeDefaultDataset(dataset: ChartDataset, i: number): number {
  dataset.borderColor = getBorderColor(i);
  dataset.backgroundColor = getBackgroundColor(i);

  return ++i;
}

function colorizeDoughnutDataset(dataset: ChartDataset, i: number): number {
  dataset.backgroundColor = dataset.data?.map(() => getBorderColor(i++)) || [];

  return i;
}

function colorizePolarAreaDataset(dataset: ChartDataset, i: number): number {
  dataset.backgroundColor = dataset.data?.map(() => getBackgroundColor(i++)) || [];

  return i;
}

function getColorizer(chart: Chart): (dataset: ChartDataset, datasetIndex: number) => void {
  let i = 0;

  return (dataset: ChartDataset, datasetIndex: number) => {
    const controller = chart.getDatasetMeta(datasetIndex).controller;

    if (controller instanceof DoughnutController) {
      i = colorizeDoughnutDataset(dataset, i);
    } else if (controller instanceof PolarAreaController) {
      i = colorizePolarAreaDataset(dataset, i);
    } else if (controller) {
      i = colorizeDefaultDataset(dataset, i);
    }
  };
}

function containsColorsDefinitions(descriptors: ColorsDescriptor[] | Record<string, ColorsDescriptor>): boolean {
  for (const k in descriptors) {
    if (descriptors[k]?.borderColor || descriptors[k]?.backgroundColor) {
      return true;
    }
  }

  return false;
}

function containsColorsDefinition(descriptor: ColorsDescriptor): boolean {
  return descriptor && (descriptor.borderColor || descriptor.backgroundColor);
}

export const NaikanColorsPlugin = {
  id: 'naikan-colors',

  defaults: {
    enabled: true,
    forceOverride: false
  } as ColorsPluginOptions,

  beforeLayout(chart: Chart, _args: any, options: ColorsPluginOptions): void {
    if (!options.enabled) {
      return;
    }

    const {
      data: {datasets},
      options: chartOptions
    } = chart.config;
    const {elements} = chartOptions;

    if (!options.forceOverride && (containsColorsDefinitions(datasets)
        || containsColorsDefinition(chartOptions)
        || (elements && containsColorsDefinitions(elements)))) {
      return;
    }

    const colorizer = getColorizer(chart);

    datasets.forEach(colorizer);
  }
};


