package io.weyoui.weyouiappcore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.weyoui.weyouiappcore.group.query.application.dto.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "zip_code")
    private String zipCode;

    private Point point;

    @JsonIgnore
    public String getFullAddress() {
        return address1 + " " + address2;
    }

    @JsonIgnore
    public AddressResponse toResponseDto() {
        return new AddressResponse(getFullAddress(), zipCode, new Location(point.getCoordinate().getX(), point.getCoordinate().getY()));
    }
}
