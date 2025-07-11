package com.deepdhamala.filmpatro.auth.oneTimeCode;

import com.deepdhamala.filmpatro.user.User;

public interface OneTimeCodeService<T extends  OneTimeCode<T>> {

    T generate(User user);

    T save(T oneTimeCode);

    T issue(User user);
}
