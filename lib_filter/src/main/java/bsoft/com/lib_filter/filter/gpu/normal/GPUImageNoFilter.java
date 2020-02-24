package bsoft.com.lib_filter.filter.gpu.normal;


import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;

public class GPUImageNoFilter extends GPUImageFilter {
    public GPUImageNoFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, GPUImageFilter.NO_FILTER_FRAGMENT_SHADER);
    }
}
