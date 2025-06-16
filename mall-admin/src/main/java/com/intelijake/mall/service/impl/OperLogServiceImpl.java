package com.intelijake.mall.service.impl;

import com.intelijake.pojo.OperLog;
import com.intelijake.mall.mapper.OperLogMapper;
import com.intelijake.mall.service.IOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Operation Log Record 服务实现类
 * </p>
 *
 * @author Jake
 * @since 2025-06-16
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements IOperLogService {

}
