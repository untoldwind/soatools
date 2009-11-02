package de.objectcode.soatools.mfm.api;

public class UpgradeException extends Exception
{
  private static final long serialVersionUID = 1889086650320729989L;

  public UpgradeException(String message)
  {
    super(message);
  }

  public UpgradeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public UpgradeException(Throwable cause)
  {
    super(cause);
  }

}
