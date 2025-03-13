package cyou.oxling.loanappbackend.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信用与额度信息表
 * 
 * 字段说明：
 * id - 主键ID
 * userId - 用户ID，关联user_info表
 * creditScore - 信用分数，初始50，满分100
 * creditLimit - 当前总额度
 * usedCredit - 已使用额度
 * availableCredit - 可用额度（计算列）
 * remark - 修改说明/备注
 * createTime - 创建时间
 * updateTime - 更新时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredit {
    private Long id;
    private Long userId;
    private Integer creditScore;
    private BigDecimal creditLimit;
    private BigDecimal usedCredit;
    private BigDecimal availableCredit;
    private String remark;
    private Date createTime;
    private Date updateTime;
    
    /**
     * 计算可用额度
     * 注意：这个方法在数据库中是通过计算列实现的，这里提供一个方法用于Java代码中计算
     */
    public void calculateAvailableCredit() {
        if (creditLimit != null && usedCredit != null) {
            this.availableCredit = creditLimit.subtract(usedCredit);
        }
    }
} 