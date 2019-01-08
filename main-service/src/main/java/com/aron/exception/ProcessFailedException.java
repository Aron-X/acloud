package com.aron.exception;

/**
 * description:
 * This ProcessFailedException use to throw to TCC when process return code not equals to success.
 * change history:
 * date             defect#             person             comments
 * ---------------------------------------------------------------------------------------------------------------------
 * 2018/6/27        ********             PlayBoy               create file
 *
 * @author: PlayBoy
 * @date: 2018/6/27 14:25
 * @copyright: 2018, FA Software (Shanghai) Co., Ltd. All Rights Reserved.
 */
public class ProcessFailedException extends RuntimeException {

    private static final long serialVersionUID = 7133863454160604785L;

    public ProcessFailedException() {
    }

    public ProcessFailedException(String message) {
        super(message);
    }

}
