package com.deepdhamala.filmpatro.email.message;

public interface EmailMessage<T extends EmailMessage<T>> {

    String getTo();

    String getSubject();

    default String getContent(){
        return null;
    };

    default String getHtmlContent(){
        return null;
    };

}
