package com.z2011.blogapi.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("文章分页参数类")
public class PagParam {
    @ApiModelProperty("当前页数")
    @NotNull(message = "当前页数不能为空")
    private Integer page;
    @NotNull(message = "数量不能为空")
    @ApiModelProperty("一页的数量")
    private Integer pageSize;
    private String name;
    private String sort;
    private Long tagId;
    private Long categoryId;
    private String month;
    private String year;

    /**
     * 这里因为前端数据要与数据库的month的01一致，所以在这做month.length() == 1判断
     * 但因为也会传入month为null，这里访问null的长度报错了，所以需要加一个前置条件不为null
     * @return
     */
    public String getMonth() {
        if (month!=null && month.length() == 1) {
            month="0"+month;
        }
        return month;
    }
}
