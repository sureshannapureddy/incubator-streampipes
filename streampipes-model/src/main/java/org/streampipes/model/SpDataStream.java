package org.streampipes.model;

import org.streampipes.empire.annotations.RdfProperty;
import org.streampipes.empire.annotations.RdfsClass;
import org.apache.commons.lang.RandomStringUtils;
import org.streampipes.model.base.NamedStreamPipesEntity;
import org.streampipes.model.grounding.EventGrounding;
import org.streampipes.model.quality.EventStreamQualityDefinition;
import org.streampipes.model.quality.EventStreamQualityRequirement;
import org.streampipes.model.quality.MeasurementCapability;
import org.streampipes.model.quality.MeasurementObject;
import org.streampipes.model.schema.EventSchema;
import org.streampipes.model.util.Cloner;
import org.streampipes.vocabulary.StreamPipes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@RdfsClass(StreamPipes.DATA_STREAM)
@Entity
public class SpDataStream extends NamedStreamPipesEntity {

	private static final long serialVersionUID = -5732549347563182863L;
	
	private static final String prefix = "urn:fzi.de:eventstream:";

	@OneToMany(fetch = FetchType.EAGER,
			   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty(StreamPipes.HAS_EVENT_STREAM_QUALITY_DEFINITION)
	private transient List<EventStreamQualityDefinition> hasEventStreamQualities;

	@OneToMany(fetch = FetchType.EAGER,
			   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty(StreamPipes.HAS_EVENT_STREAM_QUALITY_REQUIREMENT)
	private transient List<EventStreamQualityRequirement> requiresEventStreamQualities;

	@OneToOne(fetch = FetchType.EAGER,
		   cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@RdfProperty(StreamPipes.HAS_GROUNDING)
	private EventGrounding eventGrounding;
	
	@OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	@RdfProperty(StreamPipes.HAS_SCHEMA)
	private EventSchema eventSchema;
	
	@RdfProperty(StreamPipes.HAS_MEASUREMENT_CAPABILTIY)
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<MeasurementCapability> measurementCapability;
	
	@RdfProperty(StreamPipes.HAS_MEASUREMENT_OBJECT)
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	private List<MeasurementObject> measurementObject;

	protected List<String> category;

	public SpDataStream(String uri, String name, String description, String iconUrl, List<EventStreamQualityDefinition> hasEventStreamQualities,
											EventGrounding eventGrounding,
											EventSchema eventSchema) {
		super(uri, name, description, iconUrl);
		this.hasEventStreamQualities = hasEventStreamQualities;
		this.eventGrounding = eventGrounding;
		this.eventSchema = eventSchema;
	}
	
	public SpDataStream(String uri, String name, String description, EventSchema eventSchema)
	{
		super(uri, name, description);
		this.eventSchema = eventSchema;
	}

	public SpDataStream() {
		super(prefix +RandomStringUtils.randomAlphabetic(6));
	}


	public SpDataStream(SpDataStream other) {
		super(other);		
		if (other.getEventGrounding() != null) this.eventGrounding = new EventGrounding(other.getEventGrounding());
		if (other.getEventSchema() != null) this.eventSchema = new EventSchema(other.getEventSchema());
		if (other.getHasEventStreamQualities() != null) this.hasEventStreamQualities = other.getHasEventStreamQualities().stream().map(s -> new EventStreamQualityDefinition(s)).collect(Collectors.toCollection(ArrayList<EventStreamQualityDefinition>::new));
		if (other.getRequiresEventStreamQualities() != null) this.requiresEventStreamQualities = other.getRequiresEventStreamQualities().stream().map(s -> new EventStreamQualityRequirement(s)).collect(Collectors.toCollection(ArrayList<EventStreamQualityRequirement>::new));
		if (other.getMeasurementCapability() != null) this.measurementCapability =  new Cloner().mc(other.getMeasurementCapability());
		if (other.getMeasurementObject() != null) this.measurementObject = new Cloner().mo(other.getMeasurementObject());
	}


	public List<EventStreamQualityDefinition> getHasEventStreamQualities() {
		return hasEventStreamQualities;
	}

	public void setHasEventStreamQualities(
			List<EventStreamQualityDefinition> hasEventStreamQualities) {
		this.hasEventStreamQualities = hasEventStreamQualities;
	}
	

	public List<EventStreamQualityRequirement> getRequiresEventStreamQualities() {
		return requiresEventStreamQualities;
	}

	public void setRequiresEventStreamQualities(
			List<EventStreamQualityRequirement> requiresEventStreamQualities) {
		this.requiresEventStreamQualities = requiresEventStreamQualities;
	}

	public EventSchema getEventSchema() {
		return eventSchema;
	}

	public void setEventSchema(EventSchema eventSchema) {
		this.eventSchema = eventSchema;
	}

	public EventGrounding getEventGrounding() {
		return eventGrounding;
	}

	public void setEventGrounding(EventGrounding eventGrounding) {
		this.eventGrounding = eventGrounding;
	}

	public List<MeasurementCapability> getMeasurementCapability() {
		return measurementCapability;
	}

	public void setMeasurementCapability(
			List<MeasurementCapability> measurementCapability) {
		this.measurementCapability = measurementCapability;
	}

	public List<MeasurementObject> getMeasurementObject() {
		return measurementObject;
	}

	public void setMeasurementObject(List<MeasurementObject> measurementObject) {
		this.measurementObject = measurementObject;
	}

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}