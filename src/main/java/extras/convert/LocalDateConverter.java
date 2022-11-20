package extras.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate date) {
    return Date.valueOf(date);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date date) {
    return date.toLocalDate();
  }
}