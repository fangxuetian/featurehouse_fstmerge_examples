

package net.sf.freecol.common.option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.sf.freecol.client.gui.i18n.Messages;


public class AudioMixerOption extends AbstractOption {

    private static Logger logger = Logger.getLogger(AudioMixerOption.class.getName());

    private static final Map<String, MixerWrapper> audioMixers = new HashMap<String, MixerWrapper>();

    public static final String AUTO = Messages.message("clientOptions.audio.audioMixer.automatic");

    private static final Mixer AUTODETECT_MIXER = tryGetMixer();
    private static final MixerWrapper DEFAULT = new MixerWrapper(AUTO,
            (AUTODETECT_MIXER != null) ? AUTODETECT_MIXER.getMixerInfo() : null);

    private static Mixer tryGetMixer() {
        Mixer mixer = null;
        try {
            mixer = AudioSystem.getMixer(null);
        } catch (IllegalArgumentException e) {
            ; 
        }
        return mixer;
    }

    
    private static Comparator<MixerWrapper> audioMixerComparator = new Comparator<MixerWrapper>() {
        public int compare(MixerWrapper m1, MixerWrapper m2) {
            if (m1.equals(DEFAULT)) {
                if (m2.equals(DEFAULT)) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (m2.equals(DEFAULT)) {
                return 1;
            } else {
                return m1.getMixerInfo().getName().compareTo(m2.getMixerInfo().getName());
            }
        }
    };

    private MixerWrapper value;

    
    public AudioMixerOption(String id) {
        this(id, null);
    }

    public AudioMixerOption(String id, OptionGroup optionGroup) {
        super(id, optionGroup);
        value = DEFAULT;
    }

    
    
    public final MixerWrapper getValue() {
        return value;
    }

    
    public final void setValue(final MixerWrapper newValue) {
        final MixerWrapper oldValue = this.value;
        this.value = newValue;
        if (!newValue.equals(oldValue)) {
            firePropertyChange("value", oldValue, value);
        }
    }

    
    public MixerWrapper[] getOptions() {
        findAudioMixers();
        List<MixerWrapper> mixers = new ArrayList<MixerWrapper>(audioMixers.values());
        Collections.sort(mixers, audioMixerComparator);
        return mixers.toArray(new MixerWrapper[0]);
    }

    
    private void findAudioMixers() {
        audioMixers.clear();
        audioMixers.put(AUTO, DEFAULT);
        for (Mixer.Info mi : AudioSystem.getMixerInfo()) {
            audioMixers.put(mi.getName(), new MixerWrapper(mi.getName(), mi));
        }
    }
    
    
    protected void toXMLImpl(XMLStreamWriter out) throws XMLStreamException {
        
        out.writeStartElement(getId());

        out.writeAttribute("value", getValue().getKey());

        out.writeEndElement();
    }
    
    
    protected void readFromXMLImpl(XMLStreamReader in) throws XMLStreamException {
        findAudioMixers();
        final MixerWrapper oldValue = this.value;
        
        MixerWrapper newValue = audioMixers.get(in.getAttributeValue(null, "value"));
        if (newValue == null) {
            newValue = audioMixers.get(AUTO);
        }
        setValue(newValue);
        in.nextTag();
    }


    
    public static String getXMLElementTagName() {
        return "audioMixerOption";
    }
    
    public static class MixerWrapper {
        private String name;
        private Mixer.Info mixerInfo;
        
        MixerWrapper(String name, Mixer.Info mixerInfo) {
            this.name = name;
            this.mixerInfo = mixerInfo;
        }
        
        public String getKey() {
            return name;
        }
        
        public Mixer.Info getMixerInfo() {
            return mixerInfo;
        
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof MixerWrapper) {
                return ((MixerWrapper) o).getKey().equals(getKey());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }
    }

}
