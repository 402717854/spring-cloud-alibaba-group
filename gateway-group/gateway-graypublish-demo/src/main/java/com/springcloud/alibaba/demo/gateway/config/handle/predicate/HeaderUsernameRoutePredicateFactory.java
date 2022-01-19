package com.springcloud.alibaba.demo.gateway.config.handle.predicate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class HeaderUsernameRoutePredicateFactory extends AbstractRoutePredicateFactory<HeaderUsernameRoutePredicateFactory.Config> {

    public static final String USERNAME = "Username";

    public HeaderUsernameRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("username");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        List<String> usernames = config.getUsername();
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                String username = serverWebExchange.getRequest().getHeaders().getFirst(USERNAME);
                if (!StringUtils.isEmpty(username)) {
                    return usernames.contains(username);
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("Header: Username=%s", config.getUsername());
            }
        };
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Config {
        private List<String> username;
    }
}

