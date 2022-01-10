package kr.springboot.dcinside.cartoon.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Role {

    public final static Role USER = new Role("USER");
    public final static Role SERVICE = new Role("SERVICE");

    private String name;

    public Role(String name) {
        this.name = name;
    }

}
