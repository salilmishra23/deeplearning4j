package org.nd4j.linalg.api.ops.impl.shape.tensorops;

import lombok.val;
import onnx.OnnxProto3;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;
import org.nd4j.list.compat.TensorList;

import java.util.Map;

public class TensorArrayScatterV3 extends BaseTensorOp {
    public TensorArrayScatterV3(String name, SameDiff sameDiff, SDVariable[] args){
        super(name, sameDiff, args);
    }
    public TensorArrayScatterV3(SameDiff sameDiff, SDVariable[] args){
        super(null, sameDiff, args);
    }

    public TensorArrayScatterV3(){}

    @Override
    public String tensorflowName() {
        return "TensorArrayScatterV3";
    }

    @Override
    public TensorList execute(SameDiff sameDiff) {
        val list = getList(sameDiff);

        INDArray indices = this.getArgumentArray(1);
        val source = this.getArgumentArray(2);
        if(indices.length() == 1 && indices.getInt(0) == -1){
            indices = Nd4j.create(ArrayUtil.toDouble(ArrayUtil.range(0, source.shape()[0])), new long[]{source.shape()[0]});
        }
        val axis = ArrayUtil.range(1, source.rank());

        for (int e = 0; e < indices.length(); e++) {
            val cIdx = indices.getInt(e);

            val array = source.tensorAlongDimension(cIdx, axis).dup(source.ordering());
            list.put(cIdx, array);
        }

        return list;
    }

    @Override
    public String toString() {
        return opName();
    }

    @Override
    public String opName() {
        return "tensorarrayscatterv3";
    }


    @Override
    public void initFromOnnx(OnnxProto3.NodeProto node, SameDiff initWith, Map<String, OnnxProto3.AttributeProto> attributesForNode, OnnxProto3.GraphProto graph) {
    }
}
