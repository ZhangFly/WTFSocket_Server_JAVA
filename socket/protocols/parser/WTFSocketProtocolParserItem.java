package wtf.socket.protocols.parser;

import wtf.socket.annotations.Necessary;
import wtf.socket.annotations.Option;
import wtf.socket.annotations.Version;

import wtf.socket.protocols.templates.WTFSocketProtocol;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class WTFSocketProtocolParserItem {

    private String version = null;
    private List<String> necessaryAttrs = new ArrayList<>();
    private List<String> optionAttrs = new ArrayList<>();
    private Class<? extends WTFSocketProtocol> pClass = null;

    WTFSocketProtocolParserItem(Class<? extends WTFSocketProtocol> pClass) {
        this.pClass = pClass;
        this.version = pClass.getAnnotation(Version.class).value();

        Field[] fields = this.pClass.getDeclaredFields();

        for (final Field field : fields) {
            if (field.isAnnotationPresent(Option.class)) {
                optionAttrs.add(field.getName());
            }
            if (field.isAnnotationPresent(Necessary.class)) {
                necessaryAttrs.add(field.getName());
            }
        }
    }

    String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    List<String> getNecessaryAttrs() {
        return necessaryAttrs;
    }

    List<String> getOptionAttrs() {
        return optionAttrs;
    }

    Class<? extends WTFSocketProtocol> getpClass() {
        return pClass;
    }

}
